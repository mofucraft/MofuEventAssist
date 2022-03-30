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
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import page.nafuchoco.mofu.mofueventassist.automation.EventAutomationAction;
import page.nafuchoco.mofu.mofueventassist.command.CreateCommand;
import page.nafuchoco.mofu.mofueventassist.command.HelpCommand;
import page.nafuchoco.mofu.mofueventassist.command.SubCommandExecutor;
import page.nafuchoco.mofu.mofueventassist.database.DatabaseConnector;
import page.nafuchoco.mofu.mofueventassist.database.EventsTable;
import page.nafuchoco.mofu.mofueventassist.editor.EditorClickEventListener;
import page.nafuchoco.mofu.mofueventassist.editor.EditorInputEventListener;
import page.nafuchoco.mofu.mofueventassist.editor.EditorMenuHolder;
import page.nafuchoco.mofu.mofueventassist.editor.EventEditor;
import page.nafuchoco.mofu.mofueventassist.editor.actions.select.SetStartDateAction;
import page.nafuchoco.mofu.mofueventassist.event.GameEventEndEvent;
import page.nafuchoco.mofu.mofueventassist.event.GameEventPlayerEntryEvent;
import page.nafuchoco.mofu.mofueventassist.event.GameEventStartEvent;
import page.nafuchoco.mofu.mofueventassist.event.GameEventStatusUpdateEvent;
import page.nafuchoco.mofu.mofueventassist.utils.CalendarInventoryGenerator;

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

    private GameEventRegistry eventRegistry;
    private EditorInputEventListener inputListener;


    public static MofuEventAssist getInstance() {
        if (instance == null)
            instance = (MofuEventAssist) Bukkit.getServer().getPluginManager().getPlugin("MofuEventAssist");
        return instance;
    }

    public EventAssistConfig getEventAssistConfig() {
        if (config == null)
            config = configLoader.getConfig();
        return config;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        configLoader = new ConfigLoader();
        configLoader.reloadConfig();

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
            eventRegistry = new GameEventRegistry(eventsTable);
        } catch (SQLException e) {
            getLogger().log(Level.WARNING, "An error occurred while initializing the database table.", e);
            setEnabled(false);
            return;
        }

        inputListener = new EditorInputEventListener();
        getServer().getPluginManager().registerEvents(new EditorClickEventListener(), this);
        getServer().getPluginManager().registerEvents(inputListener, this);

        // イベント開始・終了の定期確認
        Bukkit.getServer().getScheduler().runTaskTimer(this, new EventTimer(getEventRegistry()), 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getServer().getScheduler().cancelTasks(this);
    }


    private static final SubCommandExecutor HELP_COMMAND_EXECUTOR = new HelpCommand();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equals("test")) {
            if (args.length >= 1 && sender instanceof Player player) {
                EventEditor editor = new EventEditor();
                EditorMenuHolder holder = new EditorMenuHolder(editor);
                switch (args[0]) {
                    case "minutesCalendar":
                        player.openInventory(CalendarInventoryGenerator.getMinutesSelector(holder, SetStartDateAction.class));
                        break;

                    case "hourCalendar":
                        player.openInventory(CalendarInventoryGenerator.getHourSelector(holder, SetStartDateAction.class));
                        break;

                    case "dayCalender":
                        player.openInventory(CalendarInventoryGenerator.getDateSelector(holder, SetStartDateAction.class, 2022, 3));
                        break;

                    case "monthCalendar":
                        player.openInventory(CalendarInventoryGenerator.getMonthSelector(holder, SetStartDateAction.class));
                        break;

                    case "yearCalender":
                        player.openInventory(CalendarInventoryGenerator.getYearSelector(holder, SetStartDateAction.class));
                        break;

                    case "AMPMCalendar":
                        player.openInventory(CalendarInventoryGenerator.getAMPMSelector(holder, SetStartDateAction.class));
                        break;

                    default:
                        break;
                }
                return true;
            }
        } else {
            SubCommandExecutor executor = getSubCommand(args);

            if (executor != null)
                return executor.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            else
                return HELP_COMMAND_EXECUTOR.onCommand(sender, command, label, args);
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getLabel().equals("test")) {


        } else {
            SubCommandExecutor executor = getSubCommand(args);
            if (executor != null)
                return executor.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
        }
        return null;
    }

    private SubCommandExecutor getSubCommand(@NotNull String[] args) {
        SubCommandExecutor executor = null;

        if (args.length != 0) {
            switch (args[0]) {
                case "register":
                    executor = new CreateCommand();
                    break;
            }
        }
        return executor;
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameEventStatusUpdateEvent(GameEventStatusUpdateEvent event) {
        try {
            eventsTable.updateEventStatus(event.getGameEvent());
            getEventRegistry().changeEventStatus(event.getGameEvent(), event.getOldStatus(), event.getNewStatus());
        } catch (SQLException e) {
            getLogger().log(
                    Level.WARNING,
                    "An error has occurred while updating event information.",
                    e
            );
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameEventStartEvent(GameEventStartEvent event) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            event.getGameEvent().getEventOptions().getStartAutomation().getActions().forEach(EventAutomationAction::execute);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameEventEndEvent(GameEventEndEvent event) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            event.getGameEvent().getEventOptions().getEndAutomation().getActions().forEach(EventAutomationAction::execute);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameEventPlayerEntryEvent(GameEventPlayerEntryEvent event) {
        event.getGameEvent().getEntrant().add(event.getPlayer().getUniqueId());
        try {
            eventsTable.updateEntrantPlayer(event.getGameEvent());
        } catch (JsonProcessingException | SQLException e) {
            getLogger().log(
                    Level.WARNING,
                    "An error has occurred while updating event information.",
                    e
            );
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        getEventRegistry().clearEditor(event.getPlayer());
    }


    public GameEventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public EditorInputEventListener getInputListener() {
        return inputListener;
    }
}
