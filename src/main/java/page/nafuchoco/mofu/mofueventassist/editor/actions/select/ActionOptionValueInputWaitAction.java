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
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.editor.EventEditor;
import page.nafuchoco.mofu.mofueventassist.editor.actions.BaseEventEditorAction;
import page.nafuchoco.mofu.mofueventassist.editor.actions.input.SettingActionOptionValueAction;

public class ActionOptionValueInputWaitAction extends BaseEventEditorAction {

    public ActionOptionValueInputWaitAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            MofuEventAssist.getInstance().getInputListener().registerWaitingAction(player, getEditor());
            getEditor().setWaitingAction(new SettingActionOptionValueAction(getEditor()));
            var optionName = PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName());
            ((SettingActionOptionValueAction) getEditor().getWaitingAction()).setOptionName(optionName);
            player.sendMessage(Component.text("Enter the optional settings and complete the setup.").color(NamedTextColor.GREEN));
        }
    }
}
