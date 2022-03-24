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

package page.nafuchoco.mofu.mofueventassist.editor.actions;

import org.bukkit.event.inventory.InventoryClickEvent;
import page.nafuchoco.mofu.mofueventassist.editor.BaseEventEditorAction;
import page.nafuchoco.mofu.mofueventassist.editor.EventEditor;

public class SetLocationAction extends BaseEventEditorAction {

    public SetLocationAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        getEditor().getBuilder().setEventLocation(event.getWhoClicked().getLocation());
    }
}
