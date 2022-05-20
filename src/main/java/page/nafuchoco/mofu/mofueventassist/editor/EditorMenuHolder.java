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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.editor.actions.BaseEventEditorAction;
import page.nafuchoco.mofu.mofueventassist.editor.actions.EventEditorAction;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class EditorMenuHolder implements InventoryHolder {
    private final EventEditor editor;
    private String editorName = "Event Editor Menu";
    private int size = 27;
    private Inventory inventory;


    public EditorMenuHolder(EventEditor editor) {
        this.editor = editor;
    }

    public EditorMenuHolder(EventEditor editor, String editorName) {
        this.editor = editor;
        this.editorName = editorName;
    }

    public EditorMenuHolder(EventEditor editor, String editorName, int size) {
        this.editor = editor;
        this.editorName = editorName;
        this.size = size;
    }


    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (inventory == null)
            inventory = Bukkit.getServer().createInventory(this, size, editorName);
        return inventory;
    }

    public EventEditor getEditor() {
        return editor;
    }


    private final Map<Integer, EventEditorAction> menuActionMap = new HashMap<>();

    public void addMenu(int index, @NotNull ItemStack itemStack, @Nullable Class<? extends BaseEventEditorAction> editorActionClass) {
        EventEditorAction editorAction = null;
        try {
            if (editorActionClass != null)
                editorAction = editorActionClass.getConstructor(EventEditor.class).newInstance(getEditor());
            getInventory().setItem(index, itemStack);
            menuActionMap.put(index, editorAction);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            MofuEventAssist.getInstance().getLogger().log(
                    Level.WARNING,
                    "Failed to initialize EventEditorAction.\n" +
                            "Incorrectly formatted class may have been specified.",
                    e
            );
        }
    }

    public void deleteMenu(int index) {
        getInventory().setItem(index, null);
        menuActionMap.remove(index);
    }

    public @Nullable EventEditorAction getAction(int index) {
        return menuActionMap.get(index);
    }
}
