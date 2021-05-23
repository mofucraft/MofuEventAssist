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

import page.nafuchoco.mofu.mofueventassist.database.EventsTable;
import page.nafuchoco.mofu.mofueventassist.element.GameEvent;

import java.sql.SQLException;
import java.util.List;

public class GameEventManager {
    private static final EventsTable eventsTable = MofuEventAssist.getInstance().getEventsTable();
    private final List<GameEvent> upcomingEvents;
    private final List<GameEvent> currentEvents;

    public GameEventManager() throws SQLException {
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

    }

    public void registerEvent(GameEvent event) {

    }

    public void deleteEvent(GameEvent event) {

    }
}
