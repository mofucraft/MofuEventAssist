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

package page.nafuchoco.mofu.mofueventassist.utils;

import lombok.val;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import page.nafuchoco.mofu.mofueventassist.editor.BaseEventEditorAction;
import page.nafuchoco.mofu.mofueventassist.editor.EditorMenuHolder;
import page.nafuchoco.mofu.mofueventassist.editor.actions.SetStartDateAction;

import java.util.Calendar;

public class CalendarInventoryGenerator {

    private CalendarInventoryGenerator() {
        throw new UnsupportedOperationException();
    }

    public static Inventory getYearSelector(EditorMenuHolder holder, Class<? extends BaseEventEditorAction> editorActionClass) {
        holder.setEditorName("Year Selector");
        holder.setSize(9);
        var inventory = holder.getInventory();

        var year = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < 9; i++) {
            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text().content(String.valueOf(year)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(i, itemStack, editorActionClass);
            year++;
        }

        return inventory;
    }

    public static Inventory getMonthSelector(EditorMenuHolder holder, Class<? extends BaseEventEditorAction> editorActionClass) {
        holder.setEditorName("Month Selector");
        holder.setSize(36);
        var inventory = holder.getInventory();

        // - - -  1  2  3 - - -
        // - - -  4  5  6 - - -
        // - - -  7  8  9 - - -
        // - - - 10 11 12 - - -
        // 3 - 4 - 5 - 12 - 13 - 14 - 21 - 22 - 23 - 30 - 31 - 32
        int line = 0;
        int index = 2;
        for (int month = 1; month <= 12; month++) {
            if (index == 9 * line + 5) {
                index = index + 7;
                line++;
            } else {
                index++;
            }

            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, month);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text().content(String.valueOf(month)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(index, itemStack, editorActionClass);
        }

        return inventory;
    }

    public static Inventory getDateSelector(EditorMenuHolder holder, Class<? extends BaseEventEditorAction> editorActionClass, int year, int month) {
        holder.setEditorName("Date Selector");
        holder.setSize(45);
        var inventory = holder.getInventory();

        // -  1  2  3  4  5  6  7 -
        // -  8  9 10 11 12 13 14 -
        // - 15 16 17 18 19 20 21 -
        // - 22 23 24 25 26 27 28 -
        // - 29 30 31  -  -  -  - -
        // 7 - 16 - 25 -
        var calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        int line = 0;
        int index = 0;
        int date = 1;
        while (date <= calendar.getActualMaximum(Calendar.DATE)) {
            if (index == line * 9 || index == (line + 1) * 9 - 1)
                index++;

            if (Math.floorMod(index, 9) == 0) {
                line++;
                continue;
            }

            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, date);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text().content(String.valueOf(date)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(index, itemStack, editorActionClass);

            date++;
            index++;
        }

        return inventory;
    }

    public static Inventory getHourSelector(EditorMenuHolder holder, Class<? extends BaseEventEditorAction> editorActionClass) {
        holder.setEditorName("Hour Selector");
        holder.setSize(36);
        var inventory = holder.getInventory();

        // - - -  1  2  3 - - -
        // - - -  4  5  6 - - -
        // - - -  7  8  9 - - -
        // - - - 10 11 12 - - -
        int line = 0;
        int index = 2;
        for (int hour = 1; hour <= 12; hour++) {
            if (index == 9 * line + 5) {
                index = index + 7;
                line++;
            } else {
                index++;
            }

            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, hour);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text().content(String.valueOf(hour)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(index, itemStack, editorActionClass);
        }

        return inventory;
    }

    public static Inventory getAMPMSelector(EditorMenuHolder holder, Class<? extends BaseEventEditorAction> editorActionClass) {
        holder.setEditorName("AM/PM Selector");
        holder.setSize(9);
        var inventory = holder.getInventory();

        var itemStackA = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        val metaA = itemStackA.getItemMeta();
        metaA.displayName(Component.text().content("AM").asComponent());
        itemStackA.setItemMeta(metaA);
        holder.addMenu(3, itemStackA, SetStartDateAction.class);
        var itemStackP = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        val metaP = itemStackP.getItemMeta();
        metaP.displayName(Component.text().content("PM").asComponent());
        itemStackP.setItemMeta(metaP);
        holder.addMenu(5, itemStackP, editorActionClass);

        return inventory;
    }

    public static Inventory getMinutesSelector(EditorMenuHolder holder, Class<? extends BaseEventEditorAction> editorActionClass) {
        holder.setEditorName("Minutes Selector");
        holder.setSize(9);
        var inventory = holder.getInventory();

        for (int minutes = 0; minutes <= 4; minutes++) {

            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, minutes == 0 ? 0 : minutes * 15);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text().content(String.valueOf(minutes)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(minutes, itemStack, editorActionClass);
        }

        return inventory;
    }
}
