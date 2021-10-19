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

package page.nafuchoco.mofu.mofueventassist;

import org.bukkit.Bukkit;
import page.nafuchoco.mofu.mofueventassist.element.GameEventStatus;
import page.nafuchoco.mofu.mofueventassist.event.GameEventEndEvent;
import page.nafuchoco.mofu.mofueventassist.event.GameEventStartEvent;

import java.util.Date;

public class EventTimer implements Runnable {
    private final GameEventRegistry eventRegistry;

    public EventTimer(GameEventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Override
    public void run() {
        var date = new Date();
        eventRegistry.getEvents(GameEventStatus.UPCOMING).forEach(event -> {
            if (event.getEventStartTime() < date.getTime())
                Bukkit.getServer().getPluginManager().callEvent(new GameEventStartEvent(event));
        });

        eventRegistry.getEvents(GameEventStatus.HOLDING).forEach(event -> {
            if (event.getEventEndTime() == 0 || event.getEventEndTime() > date.getTime())
                Bukkit.getServer().getPluginManager().callEvent(new GameEventEndEvent(event));
        });
    }
}
