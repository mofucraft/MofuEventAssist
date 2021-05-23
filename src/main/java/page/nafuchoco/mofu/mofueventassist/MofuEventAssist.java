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

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import page.nafuchoco.mofu.mofueventassist.database.DatabaseConnector;
import page.nafuchoco.mofu.mofueventassist.database.EventsTable;

import java.sql.SQLException;
import java.util.logging.Level;

public final class MofuEventAssist extends JavaPlugin {
    private static MofuEventAssist instance;
    private static ConfigLoader configLoader;
    private static EventAssistConfig config;

    private static DatabaseConnector connector;
    private static EventsTable eventsTable;

    private static GameEventManager eventManager;


    public static MofuEventAssist getInstance() {
        if (instance == null)
            instance = (MofuEventAssist) Bukkit.getServer().getPluginManager().getPlugin("MofuEventAssist");
        return instance;
    }

    public static EventAssistConfig getEventAssistConfig() {
        if (config != null)
            configLoader.getConfig();
        return config;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        configLoader = new ConfigLoader();

        // SQL Initialization
        connector = new DatabaseConnector(getEventAssistConfig().getDatabaseType(),
                getEventAssistConfig().getAddress() + ":" + getEventAssistConfig().getPort(),
                getEventAssistConfig().getDatabase(),
                getEventAssistConfig().getUsername(),
                getEventAssistConfig().getPassword());
        eventsTable = new EventsTable("events", connector);
        try {
            eventsTable.createTable();
            // Load events
            eventManager = new GameEventManager();
        } catch (SQLException e) {
            getInstance().getLogger().log(Level.WARNING, "An error occurred while initializing the database table.", e);
            setEnabled(false);
            return;
        }

        // イベント開始・終了の定期確認
        Bukkit.getServer().getScheduler().runTaskTimer(this, new EventTimer(), 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public EventsTable getEventsTable() {
        return eventsTable;
    }

    public GameEventManager getEventManager() {
        return eventManager;
    }
}
