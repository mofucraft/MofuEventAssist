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

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import page.nafuchoco.mofu.mofueventassist.event.GameEventStatusUpdateEvent;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
public class DefaultGameEvent implements GameEvent {
    private final UUID eventId;
    private final String eventName;
    private final String eventDescription;
    private final UUID eventOwner;
    private GameEventStatus eventStatus;
    private final long eventStartTime;
    private final long eventEndTime;
    private final Location eventLocation;
    private final List<UUID> entrant;
    private final EventOptions eventOptions;

    public void setEventStatus(GameEventStatus eventStatus) {
        var oldStatus = GameEventStatus.valueOf(eventStatus.name());
        this.eventStatus = eventStatus;
        Bukkit.getServer().getPluginManager().callEvent(new GameEventStatusUpdateEvent(this, oldStatus, eventStatus));
    }
}
