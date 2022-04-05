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
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.editor.EventEditor;
import page.nafuchoco.mofu.mofueventassist.editor.actions.BaseEventEditorAction;

import java.util.UUID;

public class EntryEventAction extends BaseEventEditorAction {

    public EntryEventAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!event.getCurrentItem().getItemMeta().lore().isEmpty()) {
                var eventId = event.getCurrentItem().getItemMeta().lore().stream()
                        .map(lore -> PlainTextComponentSerializer.plainText().serialize(lore))
                        .filter(this::checkUUID)
                        .map(UUID::fromString)
                        .findFirst().orElse(null);

                var gameEvent = MofuEventAssist.getInstance().getEventRegistry().getEvent(eventId);
                gameEvent.entryEvent(player);
            }
        }
    }

    private boolean checkUUID(String uuid) {
        return uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }
}
