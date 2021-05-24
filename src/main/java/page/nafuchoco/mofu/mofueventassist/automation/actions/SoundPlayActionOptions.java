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

import lombok.AllArgsConstructor;
import org.bukkit.*;
import page.nafuchoco.mofu.mofueventassist.automation.ActionOptions;

@AllArgsConstructor
public class SoundPlayActionOptions implements ActionOptions {
    // See below for a list of supported sounds.
    // https://papermc.io/javadocs/paper/1.16/org/bukkit/Sound.html
    private String soundType;
    // See below for a list of supported sound categories.
    // https://papermc.io/javadocs/paper/1.16/org/bukkit/SoundCategory.html
    private String soundCategory;
    private String location; // TODO: 2021/05/20 プレイヤーの場所からの相対位置を指定できるように
    private String volume;
    private String pitch;


    public Sound getSoundType() {
        return Sound.valueOf(soundType);
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.valueOf(soundCategory);
    }

    public Location getLocation() {
        String[] loc = location.split(", ");
        World world = Bukkit.getWorld(loc[0]);
        return new Location(world, Double.valueOf(loc[1]), Double.valueOf(loc[2]), Double.valueOf(loc[3]));
    }

    public float getVolume() {
        return Float.parseFloat(volume);
    }

    public float getPitch() {
        return Float.parseFloat(pitch);
    }
}
