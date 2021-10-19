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

package page.nafuchoco.mofu.mofueventassist.editor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;

import java.util.logging.Level;

public class EditorClickEventListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        var holder = event.getClickedInventory().getHolder();
        if (holder instanceof EditorInventoryHolder) {
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) // ほかインベントリの移動は問答無用でキャンセルする
                event.setCancelled(true);

            var editor = (EditorInventoryHolder) holder;
            try {
                editor.getEventEditorAction().execute(event);
            } catch (Exception e) {
                MofuEventAssist.getInstance().getLogger().log(
                        Level.WARNING,
                        "An error occurred while executing the editor action.",
                        e
                );
            }
        }
    }
}