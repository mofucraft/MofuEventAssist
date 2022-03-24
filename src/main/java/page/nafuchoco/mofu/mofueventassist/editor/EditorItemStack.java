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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EditorItemStack extends ItemStack {
    private final EventEditorAction editorAction;

    public EditorItemStack(EventEditorAction editorAction) {
        this.editorAction = editorAction;
    }

    public EditorItemStack(@NotNull Material type, EventEditorAction editorAction) {
        super(type);
        this.editorAction = editorAction;
    }

    public EditorItemStack(@NotNull Material type, int amount, EventEditorAction editorAction) {
        super(type, amount);
        this.editorAction = editorAction;
    }

    public EditorItemStack(@NotNull ItemStack stack, EventEditorAction editorAction) throws IllegalArgumentException {
        super(stack);
        this.editorAction = editorAction;
    }

    public EventEditorAction geEditorAction() {
        return editorAction;
    }
}
