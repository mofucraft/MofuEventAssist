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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import page.nafuchoco.mofu.mofueventassist.automation.AutomationActionContext;
import page.nafuchoco.mofu.mofueventassist.automation.actions.AutomationAction;
import page.nafuchoco.mofu.mofueventassist.command.CreateCommand;
import page.nafuchoco.mofu.mofueventassist.command.EventCommand;
import page.nafuchoco.mofu.mofueventassist.command.HelpCommand;
import page.nafuchoco.mofu.mofueventassist.command.SubCommandExecutor;
import page.nafuchoco.mofu.mofueventassist.database.DatabaseConnector;
import page.nafuchoco.mofu.mofueventassist.database.EventsTable;
import page.nafuchoco.mofu.mofueventassist.editor.EditorClickEventListener;
import page.nafuchoco.mofu.mofueventassist.editor.EditorInputEventListener;
import page.nafuchoco.mofu.mofueventassist.event.GameEventEndEvent;
import page.nafuchoco.mofu.mofueventassist.event.GameEventPlayerEntryEvent;
import page.nafuchoco.mofu.mofueventassist.event.GameEventStartEvent;
import page.nafuchoco.mofu.mofueventassist.event.GameEventStatusUpdateEvent;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public final class MofuEventAssist extends JavaPlugin implements Listener {
    private static MofuEventAssist instance;
    private ConfigLoader configLoader;
    private EventAssistConfig config;

    private DatabaseConnector connector;
    private EventsTable eventsTable;

    private Map<String, SubCommandExecutor> subCommands;
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

            // Clean Up Ended Events
            var events = new ArrayList<>();
            events.addAll(eventsTable.getUpcomingEvents());
            events.addAll(eventsTable.getHoldingEvents());

            // Load events
            eventRegistry = new GameEventRegistry(eventsTable);
        } catch (SQLException e) {
            getLogger().log(Level.WARNING, "An error occurred while initializing the database table.", e);
            setEnabled(false);
            return;
        }

        // Command Initialization
        subCommands = new HashMap<>();
        subCommands.put("register", new CreateCommand());
        subCommands.put("event", new EventCommand());

        inputListener = new EditorInputEventListener();
        getServer().getPluginManager().registerEvents(new EditorClickEventListener(), this);
        getServer().getPluginManager().registerEvents(inputListener, this);
        getServer().getPluginManager().registerEvents(this, this);

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
        SubCommandExecutor executor = getSubCommand(args);

        if (executor != null)
            return executor.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        else
            return HELP_COMMAND_EXECUTOR.onCommand(sender, command, label, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        SubCommandExecutor executor = getSubCommand(args);
        if (executor != null)
            return executor.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
        return null;
    }

    private SubCommandExecutor getSubCommand(@NotNull String[] args) {
        SubCommandExecutor executor = null;

        if (args.length != 0)
            executor = subCommands.get(args[0]);
        return executor;
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameEventStatusUpdateEvent(GameEventStatusUpdateEvent event) {
        try {
            eventsTable.updateEventStatus(event.getGameEvent());
            getLogger().info("Updated event status: " + event.getGameEvent().getEventName() + "(" + event.getOldStatus().name() + " -> " + event.getNewStatus().name() + ")");
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
            getLogger().info("Perform event start automation: " + event.getGameEvent().getEventName());
            if (event.getGameEvent().getEventOptions().getStartAutomation() != null) {
                for (AutomationAction automationAction : event.getGameEvent().getEventOptions().getStartAutomation().getActions()) {
                    automationAction.execute(new AutomationActionContext(event.getGameEvent()));
                    try {
                        Thread.sleep(event.getGameEvent().getEventOptions().getStartAutomation().getAutomationDelayTime() * 1000);
                    } catch (InterruptedException e) {
                        getLogger().log(Level.WARNING,
                                """
                                        An interrupt has occurred in the automation thread.
                                        Normally this error is not reproduced.
                                        If the problem repeats, please report it to the developer with the status of the operation.
                                        """,
                                e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        getEventRegistry().changeEventStatus(event.getGameEvent(), event.getOldStatus(), event.getNewStatus());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameEventEndEvent(GameEventEndEvent event) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            getLogger().info("Perform event end automation: " + event.getGameEvent().getEventName());
            if (event.getGameEvent().getEventOptions().getEndAutomation() != null) {
                for (AutomationAction automationAction : event.getGameEvent().getEventOptions().getEndAutomation().getActions()) {
                    automationAction.execute(new AutomationActionContext(event.getGameEvent()));
                    try {
                        Thread.sleep(event.getGameEvent().getEventOptions().getEndAutomation().getAutomationDelayTime() * 1000);
                    } catch (InterruptedException e) {
                        getLogger().log(Level.WARNING,
                                """
                                        An interrupt has occurred in the automation thread.
                                        Normally this error is not reproduced.
                                        If the problem repeats, please report it to the developer with the status of the operation.
                                        """,
                                e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        getEventRegistry().changeEventStatus(event.getGameEvent(), event.getOldStatus(), event.getNewStatus());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameEventPlayerEntryEvent(GameEventPlayerEntryEvent event) {
        event.getGameEvent().getEntrant().add(event.getPlayer().getUniqueId());
        try {
            eventsTable.updateEntrantPlayer(event.getGameEvent());
            event.getPlayer().sendMessage(ChatColor.GREEN + "You have entered the event.");
        } catch (JsonProcessingException | SQLException e) {
            getLogger().log(
                    Level.WARNING,
                    "An error has occurred while updating event information.",
                    e
            );
        }
    }


    public GameEventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public EditorInputEventListener getInputListener() {
        return inputListener;
    }
}
