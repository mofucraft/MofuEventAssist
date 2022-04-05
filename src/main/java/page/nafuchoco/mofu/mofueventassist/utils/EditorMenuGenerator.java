/*
 * Copyright 2022 NAFU_at
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

package page.nafuchoco.mofu.mofueventassist.utils;

import lombok.val;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.automation.AutomationBuilder;
import page.nafuchoco.mofu.mofueventassist.editor.EditorMenuHolder;
import page.nafuchoco.mofu.mofueventassist.editor.actions.select.*;
import page.nafuchoco.mofu.mofueventassist.element.GameEventStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class EditorMenuGenerator {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public EditorMenuGenerator() {
        throw new UnsupportedOperationException();
    }

    public static Inventory getMainMenu(EditorMenuHolder holder) {
        holder.setEditorName("Create new event");
        holder.setSize(9);

        // 名前の設定
        var nameSetStack = new ItemStack(Material.NAME_TAG);
        var nameSetStackMeta = nameSetStack.getItemMeta();
        nameSetStackMeta.displayName(Component.text("Set event name"));
        if (holder.getEditor().getBuilder().getEventName() != null)
            nameSetStackMeta.lore(List.of(Component.text(holder.getEditor().getBuilder().getEventName())));
        nameSetStack.setItemMeta(nameSetStackMeta);
        holder.addMenu(0, nameSetStack, EventNameInputWaitAction.class);

        // 説明の設定
        var descriptionSetStack = new ItemStack(Material.BOOK);
        var descriptionSetStackMeta = descriptionSetStack.getItemMeta();
        descriptionSetStackMeta.displayName(Component.text("Set event description"));
        if (holder.getEditor().getBuilder().getEventDescription() != null)
            descriptionSetStackMeta.lore(List.of(Component.text(holder.getEditor().getBuilder().getEventDescription())));
        descriptionSetStack.setItemMeta(descriptionSetStackMeta);
        holder.addMenu(1, descriptionSetStack, EventDescriptionInputWaitAction.class);

        // 開催日時の設定
        var eventStartDateSetStack = new ItemStack(Material.CLOCK);
        var eventStartDateSetStackMeta = eventStartDateSetStack.getItemMeta();
        eventStartDateSetStackMeta.displayName(Component.text("Set event start date"));
        eventStartDateSetStackMeta.lore(List.of(Component.text(dateFormat.format(holder.getEditor().getBuilder().getEventStartTime()))));
        eventStartDateSetStack.setItemMeta(eventStartDateSetStackMeta);
        holder.addMenu(2, eventStartDateSetStack, SetStartDateAction.class);

        // 開催日時の設定
        var eventEndDateSetStack = new ItemStack(Material.CLOCK);
        var eventEndDateSetStackMeta = eventEndDateSetStack.getItemMeta();
        eventEndDateSetStackMeta.displayName(Component.text("Set event end date"));
        eventEndDateSetStackMeta.lore(List.of(Component.text(dateFormat.format(holder.getEditor().getBuilder().getEventEndTime()))));
        eventEndDateSetStack.setItemMeta(eventEndDateSetStackMeta);
        holder.addMenu(3, eventEndDateSetStack, SetEndDateAction.class);

        // 開催場所の設定
        var eventLocationSetStack = new ItemStack(Material.COMPASS);
        var eventLocationSetStackMeta = eventLocationSetStack.getItemMeta();
        eventLocationSetStackMeta.displayName(Component.text("Set event location"));
        if (holder.getEditor().getBuilder().getEventLocation() != null) {
            var locationString = holder.getEditor().getBuilder().getEventLocation().toString();
            locationString = locationString.substring(9, locationString.length() - 1);
            eventLocationSetStackMeta.lore(Arrays.stream(locationString.split(",")).map(content -> Component.text(content).compact()).toList());
        }
        eventLocationSetStack.setItemMeta(eventLocationSetStackMeta);
        holder.addMenu(4, eventLocationSetStack, SetLocationAction.class);

        // 開始アクションの設定

        // 終了アクションの設定


        // 設定の保存
        if (holder.getEditor().getBuilder().canBuild()) {
            var eventSaveStack = new ItemStack(Material.WRITABLE_BOOK);
            var eventSaveStackMeta = eventSaveStack.getItemMeta();
            eventSaveStackMeta.displayName(Component.text("Save event"));
            eventSaveStack.setItemMeta(eventSaveStackMeta);
            holder.addMenu(8, eventSaveStack, EventSaveAction.class);
        }

        return holder.getInventory();
    }

    public static Inventory getActionMenu(EditorMenuHolder holder) { // TODO: 2022/04/06 組み直し
        holder.setEditorName("Create new event -> Set Event Automation");
        holder.setSize(9);

        // 開始アクションの設定
        var eventStartAutomationStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        var eventStartAutomationStackMeta = eventStartAutomationStack.getItemMeta();
        eventStartAutomationStackMeta.displayName(Component.text("Set start automation"));
        eventStartAutomationStack.setItemMeta(eventStartAutomationStackMeta);
        holder.addMenu(1, eventStartAutomationStack, null);

        // 終了アクションの設定
        var eventEndAutomationStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        var eventEndAutomationStackMeta = eventEndAutomationStack.getItemMeta();
        eventEndAutomationStackMeta.displayName(Component.text("Set end automation"));
        eventEndAutomationStack.setItemMeta(eventEndAutomationStackMeta);
        holder.addMenu(2, eventEndAutomationStack, null);

        return holder.getInventory();
    }

    public static Inventory getEditEventAutomationMenu(EditorMenuHolder holder) {
        var eventStacks = MofuEventAssist.getInstance().getEventRegistry().getEvents(GameEventStatus.UPCOMING).stream()
                .map(upcoming -> {
                    var eventStack = new ItemStack(Material.PAPER);
                    var eventStackMeta = eventStack.getItemMeta();
                    eventStackMeta.displayName(Component.text(upcoming.getEventName()));
                    eventStackMeta.lore(List.of(Component.text(upcoming.getEventId().toString())));
                    eventStack.setItemMeta(eventStackMeta);
                    return eventStack;
                })
                .toList();

        if (eventStacks.size() > 54)
            eventStacks = eventStacks.subList(0, 54);

        holder.setSize(BigDecimal.valueOf((double) eventStacks.size() / 9).setScale(0, RoundingMode.UP).intValue() * 9);
        for (int i = 0; i < eventStacks.size(); i++) {
            holder.addMenu(i, eventStacks.get(i), SettingActionOptionAction.class);
        }

        return holder.getInventory();
    }

    public static Inventory getStartEndSelectMenu(EditorMenuHolder holder) {
        holder.setEditorName("Start/End Selector");
        holder.setSize(9);

        var itemStackS = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        val metaS = itemStackS.getItemMeta();
        metaS.displayName(Component.text("Start").asComponent());
        itemStackS.setItemMeta(metaS);
        holder.addMenu(3, itemStackS, SettingActionOptionAction.class);
        var itemStackE = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        val metaE = itemStackE.getItemMeta();
        metaE.displayName(Component.text("End").asComponent());
        itemStackE.setItemMeta(metaE);
        holder.addMenu(5, itemStackE, SettingActionOptionAction.class);

        return holder.getInventory();
    }

    public static Inventory getAutomationSelectMenu(EditorMenuHolder holder) {
        var actionStacks = Arrays.stream(AutomationBuilder.AutomationActionType.values()).map(action -> {
            var actionStack = new ItemStack(Material.COMMAND_BLOCK);
            var actionStackMeta = actionStack.getItemMeta();
            actionStackMeta.displayName(Component.text(action.name()));
            actionStack.setItemMeta(actionStackMeta);
            return actionStack;
        }).toList();

        holder.setSize(BigDecimal.valueOf((double) actionStacks.size() / 9).setScale(0, RoundingMode.UP).intValue() * 9);
        for (int i = 0; i < actionStacks.size(); i++) {
            holder.addMenu(i, actionStacks.get(i), SettingActionOptionAction.class);
        }

        return holder.getInventory();
    }

    public static Inventory getActionOptionMenu(EditorMenuHolder holder) {
        var actionOptionStacks = holder.getEditor().getActionBuilder().getOptionFields().stream()
                .map(actionOption -> {
                    var actionOptionStack = new ItemStack(Material.COMMAND_BLOCK);
                    var actionOptionStackMeta = actionOptionStack.getItemMeta();
                    actionOptionStackMeta.displayName(Component.text(actionOption));
                    actionOptionStack.setItemMeta(actionOptionStackMeta);
                    return actionOptionStack;
                }).toList();

        holder.setSize(BigDecimal.valueOf((double) (actionOptionStacks.size() + 1) / 9).setScale(0, RoundingMode.UP).intValue() * 9);
        for (int i = 0; i < actionOptionStacks.size(); i++) {
            holder.addMenu(i, actionOptionStacks.get(i), ActionOptionValueInputWaitAction.class);
        }

        if (holder.getEditor().getActionBuilder().canBuild()) {
            var optionSaveStack = new ItemStack(Material.WRITABLE_BOOK);
            var optionSaveStackMeta = optionSaveStack.getItemMeta();
            optionSaveStackMeta.displayName(Component.text("Save"));
            optionSaveStack.setItemMeta(optionSaveStackMeta);
            holder.addMenu(holder.getInventory().getSize() - 1, optionSaveStack, ActionOptionSaveAction.class);
        }

        return holder.getInventory();
    }

    public static Inventory getAutomationActionListMenu(EditorMenuHolder holder) {
        holder.setEditorName("Automation Editor");
        holder.setSize(27);

        if (holder.getEditor().getAutomationBuilder().getActions().size() < 18) {
            var actionAddStack = new ItemStack(Material.COMMAND_BLOCK);
            var actionAddStackMeta = actionAddStack.getItemMeta();
            actionAddStackMeta.displayName(Component.text("Add Action"));
            actionAddStack.setItemMeta(actionAddStackMeta);
            holder.addMenu(18, actionAddStack, SettingActionOptionAction.class);
        }

        var automationSaveStack = new ItemStack(Material.WRITABLE_BOOK);
        var automationSaveStackMeta = automationSaveStack.getItemMeta();
        automationSaveStackMeta.displayName(Component.text("Save"));
        automationSaveStack.setItemMeta(automationSaveStackMeta);
        holder.addMenu(holder.getInventory().getSize() - 1, automationSaveStack, AutomationSaveAction.class);

        var actionStacks = holder.getEditor().getAutomationBuilder().getActions().stream()
                .map(action -> {
                    var actionStack = new ItemStack(Material.COMMAND_BLOCK);
                    var actionStackMeta = actionStack.getItemMeta();
                    actionStackMeta.displayName(Component.text(action.getClass().getSimpleName()));
                    actionStack.setItemMeta(actionStackMeta);
                    return actionStack;
                }).toList();
        for (int i = 0; i < actionStacks.size(); i++) {
            holder.addMenu(i, actionStacks.get(i), SettingActionOptionAction.class);
        }

        return holder.getInventory();
    }
}
