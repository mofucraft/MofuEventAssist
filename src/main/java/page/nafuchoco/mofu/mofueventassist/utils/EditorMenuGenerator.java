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
import page.nafuchoco.mofu.mofueventassist.editor.EditorMenuHolder;
import page.nafuchoco.mofu.mofueventassist.editor.actions.select.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EditorMenuGenerator {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public EditorMenuGenerator() {
        throw new UnsupportedOperationException();
    }

    public static Inventory getMainMenu(EditorMenuHolder holder) {
        holder.setEditorName("Create new event.");
        holder.setSize(9);

        // 名前の設定
        var nameSetStack = new ItemStack(Material.NAME_TAG);
        val nameSetStackMeta = nameSetStack.getItemMeta();
        nameSetStackMeta.displayName(Component.text("Set event name"));
        if (holder.getEditor().getBuilder().getEventName() != null)
            nameSetStackMeta.lore(new ArrayList<>(List.of(Component.text(holder.getEditor().getBuilder().getEventName()))));
        nameSetStack.setItemMeta(nameSetStackMeta);
        holder.addMenu(0, nameSetStack, EventNameInputWaitAction.class);

        // 説明の設定
        var descriptionSetStack = new ItemStack(Material.BOOK);
        var descriptionSetStackMeta = descriptionSetStack.getItemMeta();
        descriptionSetStackMeta.displayName(Component.text("Set event description"));
        if (holder.getEditor().getBuilder().getEventDescription() != null)
            descriptionSetStackMeta.lore(new ArrayList<>(List.of(Component.text(holder.getEditor().getBuilder().getEventDescription()))));
        descriptionSetStack.setItemMeta(descriptionSetStackMeta);
        holder.addMenu(1, descriptionSetStack, EventDescriptionInputWaitAction.class);

        // 開催日時の設定
        var eventStartDateSetStack = new ItemStack(Material.CLOCK);
        var eventStartDateSetStackMeta = eventStartDateSetStack.getItemMeta();
        eventStartDateSetStackMeta.displayName(Component.text("Set event start date"));
        eventStartDateSetStackMeta.lore(new ArrayList<>(List.of(Component.text(dateFormat.format(holder.getEditor().getBuilder().getEventStartTime())))));
        eventStartDateSetStack.setItemMeta(eventStartDateSetStackMeta);
        holder.addMenu(2, eventStartDateSetStack, SetStartDateAction.class);

        // 開催日時の設定
        var eventEndDateSetStack = new ItemStack(Material.CLOCK);
        var eventEndDateSetStackMeta = eventEndDateSetStack.getItemMeta();
        eventEndDateSetStackMeta.displayName(Component.text("Set event end date"));
        eventEndDateSetStackMeta.lore(new ArrayList<>(List.of(Component.text(dateFormat.format(holder.getEditor().getBuilder().getEventEndTime())))));
        eventEndDateSetStack.setItemMeta(eventEndDateSetStackMeta);
        holder.addMenu(3, eventEndDateSetStack, SetEndDateAction.class);

        // 開催場所の設定
        var eventLocationSetStack = new ItemStack(Material.COMPASS);
        var eventLocationSetStackMeta = eventLocationSetStack.getItemMeta();
        eventLocationSetStackMeta.displayName(Component.text("Set event location"));
        if (holder.getEditor().getBuilder().getEventLocation() != null)
            eventLocationSetStackMeta.lore(new ArrayList<>(List.of(Component.text(holder.getEditor().getBuilder().getEventLocation().toString()))));
        eventLocationSetStack.setItemMeta(eventLocationSetStackMeta);
        holder.addMenu(4, eventLocationSetStack, SetLocationAction.class);

        // 開始アクションの設定

        // 終了アクションの設定


        // 設定の保存
        var eventSaveStack = new ItemStack(Material.WRITABLE_BOOK);
        var eventSaveStackMeta = eventSaveStack.getItemMeta();
        eventSaveStackMeta.displayName(Component.text("Save event"));
        eventSaveStack.setItemMeta(eventSaveStackMeta);
        holder.addMenu(8, eventSaveStack, EventSaveAction.class);

        return holder.getInventory();
    }
}
