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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class EditorInventoryHolder implements InventoryHolder {
    private final EventEditor editor;
    private final EventEditorAction editorAction;
    private int size = 27;
    private Inventory inventory;

    public EditorInventoryHolder(EventEditor editor, EventEditorAction eventEditorAction, EventEditorAction editorAction) {
        this.editor = editor;
        this.editorAction = editorAction;
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (inventory == null)
            inventory = Bukkit.getServer().createInventory(this, size);
        return inventory;
    }

    public EventEditor getEditor() {
        return editor;
    }

    public void setMenuSize(int size) {
        this.size = size;
    }

    public EditorItemStack getMenuItemStack(Material material, int amount, Class<? extends BaseEventEditorAction> editorActionClass) {
        try {
            EventEditorAction editorAction = editorActionClass.getConstructor(EventEditor.class).newInstance(getEditor());
            return new EditorItemStack(material, amount, editorAction);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            MofuEventAssist.getInstance().getLogger().log(
                    Level.WARNING,
                    "Failed to initialize EventEditorAction.\n" +
                            "Incorrectly formatted class may have been specified.",
                    e
            );
        }
        return null;
    }

    public void addMenu(EditorItemStack itemStack, int index) {
        getInventory().setItem(index, itemStack);
    }

    public void deleteMenu(int index) {
        getInventory().setItem(index, null);
    }
}
