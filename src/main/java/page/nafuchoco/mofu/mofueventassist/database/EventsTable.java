/*
 * Copyright 2021 NAFU_at
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package page.nafuchoco.mofu.mofueventassist.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.element.DefaultGameEvent;
import page.nafuchoco.mofu.mofueventassist.element.EventOptions;
import page.nafuchoco.mofu.mofueventassist.element.GameEvent;
import page.nafuchoco.mofu.mofueventassist.element.GameEventStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class EventsTable extends DatabaseTable {
    private static final MofuEventAssist INSTANCE = MofuEventAssist.getInstance();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public EventsTable(String prefix, String tablename, DatabaseConnector connector) {
        super(prefix, tablename, connector);
    }

    public void createTable() throws SQLException {
        super.createTable("id VARCHAR(36) PRIMARY KEY, event_name VARCHAR(32) NOT NULL, description VARCHAR(120), " +
                "owner_id VARCHAR(36) NOT NULL, event_status VARCHAR(16) NOT NULL, start_date DATETIME, " +
                "end_date DATETIME DEFAULT 0, location JSON, entrant JSON, event_options JSON");
    }

    public List<GameEvent> getAllEvents(final int limit) throws SQLException {
        try (var connection = getConnector().getConnection();
             var ps = connection.prepareStatement(
                     "SELECT * FROM " + getTablename() + " LIMIT " + limit
             )) {
            try (ResultSet resultSet = ps.executeQuery()) {
                var events = new ArrayList<GameEvent>();
                while (resultSet.next())
                    events.add(parseResult(resultSet));
                return events;
            }
        } catch (JsonProcessingException e) {
            INSTANCE.getLogger().log(Level.WARNING, "An error occurred during Json processing.", e);
        }
        return new ArrayList<>();
    }

    public List<GameEvent> getUpcomingEvents() throws SQLException {
        return getSpecifiedStatusEvents(GameEventStatus.UPCOMING);
    }

    public List<GameEvent> getHoldingEvents() throws SQLException {
        return getSpecifiedStatusEvents(GameEventStatus.HOLDING);
    }

    public List<GameEvent> getEndedEvents() throws SQLException {
        return getSpecifiedStatusEvents(GameEventStatus.ENDED);
    }

    public List<GameEvent> getSpecifiedStatusEvents(GameEventStatus eventStatus) throws SQLException {
        try (var connection = getConnector().getConnection();
             var ps = connection.prepareStatement(
                     "SELECT * FROM " + getTablename() + " WHERE event_status = ?"
             )) {
            ps.setString(1, eventStatus.name());
            try (ResultSet resultSet = ps.executeQuery()) {
                var events = new ArrayList<GameEvent>();
                while (resultSet.next())
                    events.add(parseResult(resultSet));
                return events;
            }
        } catch (JsonProcessingException e) {
            INSTANCE.getLogger().log(Level.WARNING, "An error occurred during Json processing.", e);
        }
        return new ArrayList<>();
    }

    public void registerEvent(GameEvent gameEvent) throws JsonProcessingException, SQLException {
        var eventId = gameEvent.getEventId();
        var eventName = gameEvent.getEventName();
        var eventDescription = gameEvent.getEventDescription();
        var eventOwner = gameEvent.getEventOwner();
        var eventStatus = gameEvent.getEventStatus();
        var eventStartTime = gameEvent.getEventStartTime();
        var eventEndTime = gameEvent.getEventEndTime();

        String locationJson = null;
        String entrantJson = null;
        String eventOptionsJson = null;
        if (gameEvent.getEventLocation() != null)
            locationJson = MAPPER.writeValueAsString(gameEvent.getEventLocation());
        if (gameEvent.getEntrant() != null)
            entrantJson = MAPPER.writeValueAsString(gameEvent.getEntrant());
        if (gameEvent.getEventOptions() != null)
            eventOptionsJson = MAPPER.writeValueAsString(gameEvent.getEventOptions());

        try (Connection connection = getConnector().getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO " + getTablename() +
                             " (id, event_name, description, owner_id, event_status, start_date, end_date, location, entrant, event_options) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
             )) {
            ps.setString(1, eventId.toString());
            ps.setString(2, eventName);
            ps.setString(3, eventDescription);
            ps.setString(4, eventOwner.toString());
            ps.setString(5, eventStatus.name());
            ps.setDate(6, new Date(eventStartTime));
            ps.setDate(7, new Date(eventEndTime));
            ps.setString(8, locationJson);
            ps.setString(9, entrantJson);
            ps.setString(10, eventOptionsJson);

            ps.execute();
        }
    }

    public void updateEventStatus(GameEvent gameEvent) throws SQLException {
        var eventId = gameEvent.getEventId();
        var eventStatus = gameEvent.getEventStatus().name();
        try (Connection connection = getConnector().getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE " + getTablename() + " SET event_status = ? WHERE id= ?"
             )) {
            ps.setString(1, eventStatus);
            ps.setString(2, eventId.toString());

            ps.execute();
        }
    }

    public void deleteEvent(UUID eventId) throws SQLException {
        try (Connection connection = getConnector().getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM " + getTablename() + " WHERE id = ?"
             )) {
            ps.setString(1, eventId.toString());
            ps.execute();
        }
    }

    private GameEvent parseResult(ResultSet resultSet) throws JsonProcessingException, SQLException {
        var eventId = UUID.fromString(resultSet.getString("id"));
        var eventName = resultSet.getString("event_name");
        var eventDescription = resultSet.getString("description");
        var eventOwner = UUID.fromString(resultSet.getString("owner_id"));
        var eventStatus = GameEventStatus.valueOf(resultSet.getString("event_status"));
        var eventStartTime = resultSet.getDate("start_date").getTime();
        var eventEndTime = resultSet.getDate("end_date").getTime();

        var locationJson = resultSet.getString("location");
        var entrantJson = resultSet.getString("entrant");
        var eventOptionsJson = resultSet.getString("event_options");

        Location eventLocation = null;
        List<UUID> entrant = new ArrayList<>();
        EventOptions eventOptions = null;
        if (!StringUtils.isEmpty(locationJson))
            eventLocation = MAPPER.readValue(locationJson, Location.class);
        if (!StringUtils.isEmpty(entrantJson))
            entrant = MAPPER.readValue(entrantJson, new TypeReference<List<String>>() {
                    }).stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList());
        if (!StringUtils.isEmpty(eventOptionsJson))
            eventOptions = MAPPER.readValue(eventOptionsJson, EventOptions.class);

        return new DefaultGameEvent(eventId,
                eventName,
                eventDescription,
                eventOwner,
                eventStatus,
                eventStartTime,
                eventEndTime,
                eventLocation,
                entrant,
                eventOptions);
    }
}
