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

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.automation.AutomationBuilder;
import page.nafuchoco.mofu.mofueventassist.editor.EditorMenuHolder;
import page.nafuchoco.mofu.mofueventassist.editor.EventEditor;
import page.nafuchoco.mofu.mofueventassist.editor.actions.BaseEventEditorAction;
import page.nafuchoco.mofu.mofueventassist.utils.EditorMenuGenerator;

import java.util.UUID;

public class SettingActionOptionAction extends BaseEventEditorAction {

    public SettingActionOptionAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        if (getEditor().getBuiltEvent() != null) {
            Inventory nextInventory = null;
            if (getEditor().getAutomationType() == -1) {
                if (PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName()).equals("Start")) {
                    getEditor().setAutomationType(0);
                    nextInventory = EditorMenuGenerator.getAutomationSelectMenu(new EditorMenuHolder(getEditor()));
                } else if (PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName()).equals("End")) {
                    getEditor().setAutomationType(1);
                    nextInventory = EditorMenuGenerator.getAutomationSelectMenu(new EditorMenuHolder(getEditor()));
                } else {
                    nextInventory = EditorMenuGenerator.getStartEndSelectMenu(new EditorMenuHolder(getEditor()));
                }
            } else {
                if (getEditor().getActionBuilder() == null) {
                    getEditor().setActionBuilder(new AutomationBuilder.AutomationActionBuilder(getEditor().getBuiltEvent()));
                    if (getEditor().getAutomationBuilder() == null)
                        getEditor().setAutomationBuilder(new AutomationBuilder(getEditor().getBuiltEvent()));

                    var actionName = PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName());
                    try {
                        var actionType = AutomationBuilder.AutomationActionType.valueOf(actionName);
                        getEditor().getActionBuilder().setEventAutomationActionType(actionType);

                        nextInventory = EditorMenuGenerator.getActionOptionMenu(new EditorMenuHolder(getEditor()));
                    } catch (IllegalArgumentException e) {
                        nextInventory = EditorMenuGenerator.getAutomationSelectMenu(new EditorMenuHolder(getEditor()));
                    }
                }
            }

            if (nextInventory != null)
                event.getWhoClicked().openInventory(nextInventory);
        } else {
            if (!event.getCurrentItem().getItemMeta().lore().isEmpty()) {
                if (checkUUID(PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().lore().get(0)))) {
                    var selectedEventId = UUID.fromString(PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().lore().get(0)));
                    var editEvent = MofuEventAssist.getInstance().getEventRegistry().getEvent(selectedEventId);
                    if (editEvent != null) {
                        getEditor().setBuiltEvent(editEvent);
                        event.getWhoClicked().openInventory(EditorMenuGenerator.getStartEndSelectMenu(new EditorMenuHolder(getEditor())));
                    }
                }
            }
        }
    }

    private boolean checkUUID(String uuid) {
        return uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }
}
