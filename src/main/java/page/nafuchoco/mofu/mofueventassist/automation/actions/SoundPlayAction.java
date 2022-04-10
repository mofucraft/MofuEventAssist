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

package page.nafuchoco.mofu.mofueventassist.automation.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.automation.ActionOptions;
import page.nafuchoco.mofu.mofueventassist.automation.AutomationActionContext;
import page.nafuchoco.mofu.mofueventassist.element.GameEvent;

import java.util.Objects;

public class SoundPlayAction extends AutomationAction {

    public SoundPlayAction(@JsonProperty("options") ActionOptions actionOptions) {
        super(actionOptions);
    }

    @Override
    public void execute(AutomationActionContext context) { // 参加者全員に指定されたサウンドを再生する。
        SoundPlayActionOptions options = (SoundPlayActionOptions) getOptions();
        context.gameEvent().getEntrant().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(p -> Bukkit.getServer().getScheduler().runTask(MofuEventAssist.getInstance(), () -> {
                    p.playSound(
                            getLocationObject(p, context.gameEvent(), options.getLocation()),
                            options.getSoundType(),
                            options.getSoundCategory(),
                            options.getVolume(),
                            options.getPitch());
                }));
    }

    private Location getLocationObject(Player player, GameEvent event, String location) {
        Location target = player.getLocation();
        Location eventLocation = event.getEventLocation();
        String[] loc = location.split(", ");
        double x;
        double y;
        double z;

        if (loc[0].startsWith("~")) {
            x = target.getX() + Double.parseDouble(loc[1].substring(1));
        } else {
            x = Double.parseDouble(loc[1]);
        }
        if (loc[1].startsWith("~")) {
            y = target.getY() + Double.parseDouble(loc[2].substring(1));
        } else {
            y = Double.parseDouble(loc[2]);
        }
        if (loc[2].startsWith("~")) {
            z = target.getZ() + Double.parseDouble(loc[3].substring(1));
        } else {
            z = Double.parseDouble(loc[3]);
        }
        return new Location(eventLocation.getWorld(), x, y, z);
    }
}
