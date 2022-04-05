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

package page.nafuchoco.mofu.mofueventassist.editor.actions.select;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import page.nafuchoco.mofu.mofueventassist.editor.EditorMenuHolder;
import page.nafuchoco.mofu.mofueventassist.editor.EventEditor;
import page.nafuchoco.mofu.mofueventassist.editor.actions.BaseEventEditorAction;
import page.nafuchoco.mofu.mofueventassist.utils.CalendarInventoryGenerator;
import page.nafuchoco.mofu.mofueventassist.utils.EditorMenuGenerator;

import java.util.Calendar;

public class SetEndDateAction extends BaseEventEditorAction {

    public SetEndDateAction(EventEditor editor) {
        super(editor);
    }

    private int year = -1;
    private int month = -1;
    private int date = -1;
    private int hour = -1;
    private int pm = -1;
    private int minutes = -1;

    @Override
    public void execute(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof EditorMenuHolder holder) {
            if (getEditor().getWaitingAction() == this) {
                var value =
                        switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                            case "AM" -> 0;

                            case "PM" -> 1;

                            default -> Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
                        };

                Inventory nextInventory = null;
                if (year == -1) {
                    year = value;
                    nextInventory = CalendarInventoryGenerator.getMonthSelector(new EditorMenuHolder(getEditor()), this.getClass());
                } else if (month == -1) {
                    month = value;
                    nextInventory = CalendarInventoryGenerator.getDateSelector(new EditorMenuHolder(getEditor()), this.getClass(), year, month);
                } else if (date == -1) {
                    date = value;
                    nextInventory = CalendarInventoryGenerator.getHourSelector(new EditorMenuHolder(getEditor()), this.getClass());
                } else if (hour == -1) {
                    hour = value;
                    nextInventory = CalendarInventoryGenerator.getAMPMSelector(new EditorMenuHolder(getEditor()), this.getClass());
                } else if (pm == -1) {
                    pm = value;
                    nextInventory = CalendarInventoryGenerator.getMinutesSelector(new EditorMenuHolder(getEditor()), this.getClass());
                } else if (minutes == -1) {
                    minutes = value;

                    var calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month - 1);
                    calendar.set(Calendar.DATE, date);
                    calendar.set(Calendar.HOUR, hour);
                    calendar.set(Calendar.AM_PM, pm);
                    calendar.set(Calendar.MINUTE, minutes);
                    getEditor().getBuilder().setEventEndTime(calendar.getTime().getTime());

                    getEditor().setWaitingAction(null);
                    nextInventory = EditorMenuGenerator.getMainMenu(new EditorMenuHolder(getEditor()));
                }

                if (nextInventory != null)
                    event.getWhoClicked().openInventory(nextInventory);
            } else {
                getEditor().setWaitingAction(this);
                event.getWhoClicked().openInventory(CalendarInventoryGenerator.getYearSelector(new EditorMenuHolder(getEditor()), this.getClass()));
            }
        } else {
            throw new IllegalStateException("The EventEditorAction must hold an EditorMenuHolder.");
        }
    }
}
