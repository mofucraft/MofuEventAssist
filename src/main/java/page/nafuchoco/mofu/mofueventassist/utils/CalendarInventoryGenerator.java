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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Calendar;

public class CalendarInventoryGenerator {

    private CalendarInventoryGenerator() {
        throw new UnsupportedOperationException();
    }

    public static Inventory getYearSelector(InventoryHolder holder) {
        var inventory = Bukkit.createInventory(holder, 9, "Year Selector");
        var year = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < 9; i++) {
            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            itemStack.getItemMeta().setDisplayName(Integer.toString(year));
            inventory.setItem(i, itemStack);
            year++;
        }

        return inventory;
    }

    public static Inventory getMonthSelector(InventoryHolder holder) {
        var inventory = Bukkit.createInventory(holder, 36, "Month Selector");
        // - - -  1  2  3 - - -
        // - - -  4  5  6 - - -
        // - - -  7  8  9 - - -
        // - - - 10 11 12 - - -
        // 3 - 4 - 5 - 12 - 13 - 14 - 21 - 22 - 23 - 30 - 31 - 32
        int line = 1;
        int index = 3;
        for (int month = 1; month <= 12; month++) {
            if (index == 9 * (line - 1) + 5) {
                index = index + 7;
                line++;
            } else {
                index++;
            }

            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, month);
            itemStack.getItemMeta().setDisplayName(Integer.toString(month));
            inventory.setItem(index, itemStack);
        }

        return inventory;
    }

    public static Inventory getDateSelector(InventoryHolder holder, int year, int month) {
        var inventory = Bukkit.createInventory(holder, 45, "Date Selector");
        // -  1  2  3  4  5  6  7 -
        // -  8  9 10 11 12 13 14 -
        // - 15 16 17 18 19 20 21 -
        // - 22 23 24 25 26 27 28 -
        // - 29 30 31  -  -  -  - -
        // 7 - 16 - 25 -
        var calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        int line = 1;
        int index = 1;
        for (int date = 1; date <= calendar.getActualMaximum(Calendar.DATE); date++) {
            if (index == 7 * line + 2 * (line - 1)) {
                index = index + 3;
            } else {
                index++;
            }

            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, date);
            itemStack.getItemMeta().setDisplayName(Integer.toString(date));
            inventory.setItem(index, itemStack);
        }

        return inventory;
    }

    public static Inventory getHourSelector(InventoryHolder holder) {
        var inventory = Bukkit.createInventory(holder, 36, "Hour Selector");
        // - - -  1  2  3 - - -
        // - - -  4  5  6 - - -
        // - - -  7  8  9 - - -
        // - - - 10 11 12 - - -
        int line = 1;
        int index = 3;
        for (int hour = 1; hour <= 12; hour++) {
            if (index == 9 * (line - 1) + 5) {
                index = index + 7;
                line++;
            } else {
                index++;
            }

            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, hour);
            itemStack.getItemMeta().setDisplayName(Integer.toString(hour));
            inventory.setItem(index, itemStack);
        }

        return inventory;
    }

    public static Inventory getAMPMSelector(InventoryHolder holder) {
        var inventory = Bukkit.createInventory(holder, 9, "AM/PM Selector");
        var itemStackA = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        itemStackA.getItemMeta().setDisplayName("AM");
        inventory.setItem(3, itemStackA);
        var itemStackP = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        itemStackP.getItemMeta().setDisplayName("PM");
        inventory.setItem(5, itemStackP);

        return inventory;
    }

    public static Inventory getMinutesSelector(InventoryHolder holder) {
        var inventory = Bukkit.createInventory(holder, 9, "Minutes Selector");
        for (int minutes = 0; minutes <= 4; minutes++) {

            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, minutes == 0 ? 0 : minutes * 15);
            itemStack.getItemMeta().setDisplayName(Integer.toString(minutes));
            inventory.setItem(minutes, itemStack);
        }

        return inventory;
    }
}
