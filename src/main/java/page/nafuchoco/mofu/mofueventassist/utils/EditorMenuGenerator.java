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
import page.nafuchoco.mofu.mofueventassist.editor.actions.select.EventDescriptionInputWaitAction;
import page.nafuchoco.mofu.mofueventassist.editor.actions.select.EventNameInputWaitAction;
import page.nafuchoco.mofu.mofueventassist.editor.actions.select.SetEndDateAction;
import page.nafuchoco.mofu.mofueventassist.editor.actions.select.SetStartDateAction;

public class EditorMenuGenerator {

    public static Inventory getMainMenu(EditorMenuHolder holder) {
        holder.setEditorName("Create new event.");
        holder.setSize(9);

        // 名前の設定
        var nameSetStack = new ItemStack(Material.NAME_TAG);
        val nameSetStackMeta = nameSetStack.getItemMeta();
        nameSetStackMeta.displayName(Component.text("Set event name"));
        nameSetStack.setItemMeta(nameSetStackMeta);
        holder.addMenu(1, nameSetStack, EventNameInputWaitAction.class);

        // 説明の設定
        var descriptionSetStack = new ItemStack(Material.BOOK);
        var descriptionSetStackMeta = descriptionSetStack.getItemMeta();
        descriptionSetStackMeta.displayName(Component.text("Set event description"));
        descriptionSetStack.setItemMeta(descriptionSetStackMeta);
        holder.addMenu(2, descriptionSetStack, EventDescriptionInputWaitAction.class);

        // 開催日時の設定
        var eventStartDateSetStack = new ItemStack(Material.CLOCK);
        var eventStartDateSetStackMeta = eventStartDateSetStack.getItemMeta();
        eventStartDateSetStackMeta.displayName(Component.text("Set event start date"));
        eventStartDateSetStack.setItemMeta(eventStartDateSetStackMeta);
        holder.addMenu(3, eventStartDateSetStack, SetStartDateAction.class);

        // 開催日時の設定
        var eventEndDateSetStack = new ItemStack(Material.CLOCK);
        var eventEndDateSetStackMeta = eventEndDateSetStack.getItemMeta();
        eventEndDateSetStackMeta.displayName(Component.text("Set event end date"));
        eventEndDateSetStack.setItemMeta(eventEndDateSetStackMeta);
        holder.addMenu(4, eventEndDateSetStack, SetEndDateAction.class);

        return holder.getInventory();
    }
}
