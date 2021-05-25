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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import page.nafuchoco.mofu.mofueventassist.command.RegisterCommand;
import page.nafuchoco.mofu.mofueventassist.command.SubCommandExecutor;
import page.nafuchoco.mofu.mofueventassist.database.DatabaseConnector;
import page.nafuchoco.mofu.mofueventassist.database.EventsTable;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public final class MofuEventAssist extends JavaPlugin {
    private static MofuEventAssist instance;
    private ConfigLoader configLoader;
    private EventAssistConfig config;

    private DatabaseConnector connector;
    private EventsTable eventsTable;

    private GameEventManager eventManager;


    public static MofuEventAssist getInstance() {
        if (instance == null)
            instance = (MofuEventAssist) Bukkit.getServer().getPluginManager().getPlugin("MofuEventAssist");
        return instance;
    }

    public EventAssistConfig getEventAssistConfig() {
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
        eventsTable = new EventsTable(getEventAssistConfig().getTablePrefix(), "events", connector);
        try {
            eventsTable.createTable();
            // Load events
            eventManager = new GameEventManager(eventsTable);
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
        Bukkit.getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SubCommandExecutor executor = null;

        if (args.length == 0) {
            // Command help information
        } else switch (args[0]) {
            case "register":
                executor = new RegisterCommand();
                break;
        }

        if (executor != null)
            return executor.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Arrays.asList("");
    }

    public GameEventManager getEventManager() {
        return eventManager;
    }
}
