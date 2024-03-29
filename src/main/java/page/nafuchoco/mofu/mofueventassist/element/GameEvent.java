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

package page.nafuchoco.mofu.mofueventassist.element;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import page.nafuchoco.mofu.mofueventassist.event.GameEventPlayerEntryEvent;

import java.util.List;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface GameEvent {

    UUID getEventId();

    String getEventName();

    String getEventDescription();

    UUID getEventOwner();

    GameEventStatus getEventStatus();

    long getEventStartTime();

    long getEventEndTime();

    Location getEventLocation();

    List<UUID> getEntrant();

    EventOptions getEventOptions();

    default void entryEvent(Player player) {
        if (!getEntrant().contains(player.getUniqueId()))
            Bukkit.getServer().getPluginManager().callEvent(new GameEventPlayerEntryEvent(this, player));
        else
            player.sendMessage(ChatColor.RED + "This event has already been entered.");
    }
}
