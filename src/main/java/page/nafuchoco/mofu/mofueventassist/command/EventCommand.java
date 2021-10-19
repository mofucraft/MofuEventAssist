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

package page.nafuchoco.mofu.mofueventassist.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import page.nafuchoco.mofu.mofueventassist.GameEventRegistry;

public class EventCommand implements CommandExecutor {
    private final GameEventRegistry eventRegistry;

    public EventCommand(GameEventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {

        } else switch (args[0]) {
            case "editor": {
                // todo
                // イベントの設定は要素を横並びに。クリックで選択された要素の編集を開始
                // オートメーションの作成は要素が割り当てられたステンドグラスを並び替え
            }
        }
        return false;
    }
}
