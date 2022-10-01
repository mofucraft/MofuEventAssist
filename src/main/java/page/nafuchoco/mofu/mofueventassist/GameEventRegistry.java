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

package page.nafuchoco.mofu.mofueventassist;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import page.nafuchoco.mofu.mofueventassist.database.EventsTable;
import page.nafuchoco.mofu.mofueventassist.element.DefaultGameEvent;
import page.nafuchoco.mofu.mofueventassist.element.GameEvent;
import page.nafuchoco.mofu.mofueventassist.element.GameEventStatus;
import page.nafuchoco.mofu.mofueventassist.exception.EventRegisterException;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class GameEventRegistry {
    private final EventsTable eventsTable;

    private final Map<GameEventStatus, List<GameEvent>> eventStore;

    public GameEventRegistry(EventsTable eventsTable) throws SQLException {
        this.eventsTable = eventsTable;
        eventStore = new EnumMap<>(GameEventStatus.class);
        eventStore.put(GameEventStatus.UPCOMING, eventsTable.getUpcomingEvents());
        eventStore.put(GameEventStatus.HOLDING, eventsTable.getHoldingEvents());
        eventStore.put(GameEventStatus.ENDED, new ArrayList<>());
    }

    public GameEvent getEvent(UUID id) {
        return getEvents().stream().filter(event -> event.getEventId().equals(id)).findFirst().orElse(null);
    }

    public List<GameEvent> getEvents(GameEventStatus eventStatus) {
        if (eventStatus == null)
            return getEvents();
        return Collections.unmodifiableList(eventStore.get(eventStatus));
    }

    private List<GameEvent> getEvents() {
        return eventStore.keySet().stream().flatMap(key -> eventStore.get(key).stream()).toList();
    }

    public void registerEvent(@NotNull GameEvent event) {
        try {
            eventsTable.registerEvent(event);
            eventStore.get(event.getEventStatus()).add(event);
        } catch (JsonProcessingException | SQLException e) {
            MofuEventAssist.getInstance().getLogger().log(
                    Level.WARNING,
                    "An error has occurred while registering event information.",
                    e
            );

            throw new EventRegisterException("");
        }
    }

    public void updateEventOptions(@NotNull GameEvent event) {
        try {
            eventsTable.updateEventOptions(event);
        } catch (JsonProcessingException | SQLException e) {
            MofuEventAssist.getInstance().getLogger().log(
                    Level.WARNING,
                    "An error has occurred while registering event information.",
                    e
            );

            throw new EventRegisterException("");
        }
    }

    public void deleteEvent(@NotNull GameEvent event) {
        try {
            eventsTable.deleteEvent(event.getEventId());
            eventStore.get(event.getEventStatus()).remove(event);
        } catch (SQLException e) {
            MofuEventAssist.getInstance().getLogger().log(
                    Level.WARNING,
                    "An error has occurred while registering event information.",
                    e
            );

            throw new EventRegisterException("");
        }
    }

    protected void changeEventStatus(GameEvent gameEvent, GameEventStatus oldStatus, GameEventStatus newStatus) {
        if (gameEvent instanceof DefaultGameEvent event)
            event.setEventStatus(newStatus);
        eventStore.get(oldStatus).remove(gameEvent);
        eventStore.get(newStatus).add(gameEvent);
    }
}
