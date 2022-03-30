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

package page.nafuchoco.mofu.mofueventassist.editor.actions.input;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.editor.EditorMenuHolder;
import page.nafuchoco.mofu.mofueventassist.editor.EventEditor;
import page.nafuchoco.mofu.mofueventassist.editor.actions.BaseEventInputAction;
import page.nafuchoco.mofu.mofueventassist.utils.EditorMenuGenerator;

public class SetEventNameAction extends BaseEventInputAction {

    public SetEventNameAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(AsyncChatEvent event) {
        getEditor().getBuilder().setEventName(PlainTextComponentSerializer.plainText().serialize(event.message()));
        getEditor().setWaitingAction(null);
        Bukkit.getServer().getScheduler().runTask(MofuEventAssist.getInstance(),
                () -> event.getPlayer().openInventory(EditorMenuGenerator.getMainMenu(new EditorMenuHolder(getEditor()))));
    }
}
