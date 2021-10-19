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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import page.nafuchoco.mofu.mofueventassist.database.EventsTable;
import page.nafuchoco.mofu.mofueventassist.editor.EventEditor;
import page.nafuchoco.mofu.mofueventassist.element.GameEvent;
import page.nafuchoco.mofu.mofueventassist.element.GameEventStatus;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class GameEventRegistry {
    private final EventsTable eventsTable;

    private final Map<GameEventStatus, List<GameEvent>> eventStore;
    private final Map<Player, EventEditor> editorStore;

    public GameEventRegistry(EventsTable eventsTable) throws SQLException {
        this.eventsTable = eventsTable;
        eventStore = new HashMap<>();
        editorStore = new HashMap<>();
        eventStore.put(GameEventStatus.UPCOMING, eventsTable.getUpcomingEvents());
        eventStore.put(GameEventStatus.HOLDING, eventsTable.getHoldingEvents());
    }

    public List<GameEvent> getEvents(@NotNull GameEventStatus eventStatus) {
        return Collections.unmodifiableList(eventStore.get(eventStatus));
    }

    public void registerEvent(@NotNull GameEvent event) {
        try {
            eventsTable.registerEvent(event);
            eventStore.get(event.getEventStatus()).add(event);
        } catch (JsonProcessingException | SQLException e) {
            MofuEventAssist.getInstance().getLogger().log(
                    Level.WARNING,
                    "An error has occurred while registering event information.",
                    e
            );
        }
    }

    public void deleteEvent(@NotNull GameEvent event) {
        try {
            eventsTable.deleteEvent(event.getEventId());
            eventStore.get(event.getEventStatus()).remove(event);
        } catch (SQLException e) {
            MofuEventAssist.getInstance().getLogger().log(
                    Level.WARNING,
                    "An error has occurred while registering event information.",
                    e
            );
        }
    }

    protected void changeEventStatus(GameEvent gameEvent, GameEventStatus oldStatus, GameEventStatus newStatus) {
        eventStore.get(oldStatus).remove(gameEvent);
        eventStore.get(newStatus).add(gameEvent);
    }

    public void registerEditor(Player player, @NotNull EventEditor editor) {
        editorStore.put(player, editor);
    }

    public @Nullable EventEditor getEventBuilder(Player player) {
        return editorStore.get(player);
    }

    protected void clearEditor(Player player) {
        editorStore.remove(player);
    }
}
