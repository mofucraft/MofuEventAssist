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

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class GameEventBuilder {
    private final UUID eventId;
    private final EventOptions eventOptions;
    private final List<UUID> entrant;
    private String eventName;
    private String eventDescription;
    private UUID eventOwner;
    private long eventStartTime;
    private long eventEndTime;
    private Location eventLocation;

    public GameEventBuilder() {
        eventId = UUID.randomUUID();
        eventOptions = new EventOptions();
        entrant = new ArrayList<>();
    }

    public DefaultGameEvent build() {
        if (eventName == null || eventDescription == null || eventOwner == null)
            throw new IllegalStateException("The configuration required for the build has not been completed.");

        return new DefaultGameEvent(
                eventId,
                eventName,
                eventDescription,
                eventOwner,
                eventStartTime,
                eventEndTime,
                eventLocation,
                entrant,
                eventOptions
        );
    }

    public GameEventBuilder setEventName(@NonNull String eventName) {
        this.eventName = eventName;
        return this;
    }

    public GameEventBuilder setEventDescription(@NonNull String eventDescription) {
        this.eventDescription = eventDescription;
        return this;
    }

    public GameEventBuilder setEventOwner(@NonNull UUID eventOwner) {
        this.eventOwner = eventOwner;
        return this;
    }

    public GameEventBuilder setEventStartTime(long eventStartTime) {
        this.eventStartTime = eventStartTime;
        return this;
    }

    public GameEventBuilder setEventEndTime(long eventEndTime) {
        this.eventEndTime = eventEndTime;
        return this;
    }

    public GameEventBuilder setEventLocation(Location eventLocation) {
        this.eventLocation = eventLocation;
        return this;
    }
}
