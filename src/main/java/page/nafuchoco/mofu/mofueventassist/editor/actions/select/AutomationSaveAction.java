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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.editor.EditorMenuHolder;
import page.nafuchoco.mofu.mofueventassist.editor.EventEditor;
import page.nafuchoco.mofu.mofueventassist.editor.actions.BaseEventEditorAction;
import page.nafuchoco.mofu.mofueventassist.exception.EventRegisterException;
import page.nafuchoco.mofu.mofueventassist.utils.EditorMenuGenerator;

public class AutomationSaveAction extends BaseEventEditorAction {

    public AutomationSaveAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        var automation = getEditor().getAutomationBuilder().build();
        try {
            switch (getEditor().getAutomationType()) {
                case 0:
                    getEditor().getBuiltEvent().getEventOptions().setStartAutomation(automation);
                    MofuEventAssist.getInstance().getEventRegistry().updateEventOptions(getEditor().getBuiltEvent());
                    break;

                case 1:
                    getEditor().getBuiltEvent().getEventOptions().setEndAutomation(automation);
                    MofuEventAssist.getInstance().getEventRegistry().updateEventOptions(getEditor().getBuiltEvent());
                    break;

                default:
                    event.getWhoClicked().openInventory(EditorMenuGenerator.getAutomationActionListMenu(new EditorMenuHolder(getEditor())));
                    break;
            }
        } catch (EventRegisterException e) {
            event.getWhoClicked().sendMessage(Component.text("Some error occurred while saving the event.").color(NamedTextColor.RED));
        }
    }
}
