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

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.editor.EditorMenuHolder;
import page.nafuchoco.mofu.mofueventassist.editor.actions.select.EntryEventAction;
import page.nafuchoco.mofu.mofueventassist.element.GameEventStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventCommand implements SubCommandExecutor {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {

            } else switch (args[0]) {
                case "editor": {
                    // todo
                    // イベントの設定は要素を横並びに。クリックで選択された要素の編集を開始
                    // オートメーションの作成は要素が割り当てられたステンドグラスを並び替え
                }
                break;

                case "show": {
                    // 登録済みのすべてのイベントを表示する
                    var events = MofuEventAssist.getInstance().getEventRegistry().getEvents(GameEventStatus.UPCOMING);
                    var inventory = Bukkit.getServer().createInventory(player, 36, "Upcoming Events");
                    events.stream().forEach(event -> {
                        var eventItem = new ItemStack(Material.BOOK);
                        var eventItemMeta = eventItem.getItemMeta();
                        eventItemMeta.displayName(Component.text(event.getEventName()));
                        var loreList = new ArrayList<Component>();
                        loreList.add(Component.text(event.getEventDescription()));
                        loreList.add(Component.text("StartDate: " + dateFormat.format(event.getEventStartTime())));
                        loreList.add(Component.text("EndDate: " + dateFormat.format(event.getEventEndTime())));
                        loreList.add(Component.text("Owner: " + Bukkit.getPlayer(event.getEventOwner()).getName()));
                        eventItemMeta.lore(loreList);
                        eventItem.setItemMeta(eventItemMeta);
                        inventory.addItem(eventItem);
                    });

                    player.openInventory(inventory);
                }
                break;

                case "entry": {
                    // 登録済みのすべてのイベントを表示する
                    var events = MofuEventAssist.getInstance().getEventRegistry().getEvents(GameEventStatus.UPCOMING);
                    var holder = new EditorMenuHolder(null, "Upcoming Events");
                    holder.setSize(BigDecimal.valueOf((double) (events.size()) / 9).setScale(0, RoundingMode.UP).intValue() * 9);

                    for (int i = 0; i < events.size(); i++) {
                        var event = events.get(i);
                        var eventItem = new ItemStack(Material.BOOK);
                        var eventItemMeta = eventItem.getItemMeta();
                        eventItemMeta.displayName(Component.text(event.getEventName()));
                        var loreList = new ArrayList<Component>();
                        loreList.add(Component.text(event.getEventDescription()));
                        loreList.add(Component.text("StartDate: " + dateFormat.format(event.getEventStartTime())));
                        loreList.add(Component.text("EndDate: " + dateFormat.format(event.getEventEndTime())));
                        loreList.add(Component.text("Owner: " + Bukkit.getPlayer(event.getEventOwner()).getName()));
                        loreList.add(Component.text(event.getEventId().toString()));
                        eventItemMeta.lore(loreList);
                        eventItem.setItemMeta(eventItemMeta);
                        holder.addMenu(i, eventItem, EntryEventAction.class);
                    }

                    player.openInventory(holder.getInventory());
                }
                break;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be executed in-game.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
