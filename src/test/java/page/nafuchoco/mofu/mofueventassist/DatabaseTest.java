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

import org.junit.jupiter.api.Test;
import page.nafuchoco.mofu.mofueventassist.EventAssistConfig.DatabaseType;
import page.nafuchoco.mofu.mofueventassist.automation.AutomationBuilder;
import page.nafuchoco.mofu.mofueventassist.automation.EventAutomation;
import page.nafuchoco.mofu.mofueventassist.automation.actions.AutomationAction;
import page.nafuchoco.mofu.mofueventassist.database.DatabaseConnector;
import page.nafuchoco.mofu.mofueventassist.database.EventsTable;
import page.nafuchoco.mofu.mofueventassist.element.DefaultGameEvent;
import page.nafuchoco.mofu.mofueventassist.element.EventOptions;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class DatabaseTest {
    // TestDatabase Server Auth Information
    private DatabaseType databaseType = EventAssistConfig.DatabaseType.MARIADB;
    private String address = "localhost";
    private int port = 3306;
    private String database = "event_assist";
    private String username = "mofutest";
    private String password = "58g&@f3vXm%Z!Zvy";
    private String tablePrefix = "ea_";

    private DatabaseConnector connector;
    private EventsTable eventsTable;
    private GameEventManager eventManager;

    @Test
    public void saveTestdata() {
        connector = new DatabaseConnector(databaseType, address + ":" + port, database, username, password);
        eventsTable = new EventsTable(tablePrefix, "events", connector);
        try {
            eventsTable.createTable();
            // Load events
            eventManager = new GameEventManager(eventsTable);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        EventOptions eventOptions = new EventOptions();
        DefaultGameEvent gameEvent = new DefaultGameEvent(
                UUID.randomUUID(),
                "TestEvent",
                "TestEventDescription",
                UUID.randomUUID(),
                new Date().getTime(),
                0,
                null,
                Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()),
                eventOptions
        );

        AutomationBuilder.AutomationActionBuilder builder = new AutomationBuilder.AutomationActionBuilder(gameEvent);
        AutomationAction action = builder
                .setEventAutomationActionType("PLAY_SOUND")
                .setAutomationActionOption("soundType", "MUSIC_DISC_FAR")
                .setAutomationActionOption("soundCategory", "MUSIC")
                .setAutomationActionOption("location", "0,0,0")
                .setAutomationActionOption("volume", "80")
                .setAutomationActionOption("pitch", "1")
                .build();
        AutomationBuilder actionBuilder = new AutomationBuilder(gameEvent);
        EventAutomation automation = actionBuilder.addAutomationAction(action).setActionDelayTime(20).build();

        eventOptions.setStartAutomation(automation);
        eventManager.registerEvent(gameEvent);

        System.out.println(gameEvent);
    }
}
