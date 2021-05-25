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
import page.nafuchoco.mofu.mofueventassist.database.EventsTable;
import page.nafuchoco.mofu.mofueventassist.element.GameEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

public class GameEventManager {
    private final EventsTable eventsTable;
    private final List<GameEvent> upcomingEvents;
    private final List<GameEvent> currentEvents;

    public GameEventManager(EventsTable eventsTable) throws SQLException {
        this.eventsTable = eventsTable;
        upcomingEvents = eventsTable.getUpcomingEvents();
        currentEvents = eventsTable.getCurrentEvents();
    }

    public List<GameEvent> getUpcomingEvents() {
        return upcomingEvents;
    }

    public List<GameEvent> getCurrentEvents() {
        return currentEvents;
    }

    public void startEvent(GameEvent event) {
        if (event.getEventOptions().isEnableStartAnnounce())
            event.getEventOptions().getStartAutomation();
    }

    public void endEvent(GameEvent event) {
        if (event.getEventOptions().isEnableStartAnnounce())
            event.getEventOptions().getEndAutomations();
    }

    public void registerEvent(GameEvent event) {
        try {
            eventsTable.registerEvent(event);
            upcomingEvents.add(event);
        } catch (JsonProcessingException | SQLException e) {
            MofuEventAssist.getInstance().getLogger().log(
                    Level.WARNING,
                    "An error has occurred while registering event information.",
                    e
            );
        }
    }

    public void deleteEvent(GameEvent event) {
        try {
            eventsTable.deleteEvent(event.getEventId());
        } catch (SQLException e) {
            MofuEventAssist.getInstance().getLogger().log(
                    Level.WARNING,
                    "An error has occurred while registering event information.",
                    e
            );
        }
    }
}
