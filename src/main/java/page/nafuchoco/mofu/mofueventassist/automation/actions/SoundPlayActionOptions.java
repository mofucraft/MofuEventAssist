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

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import page.nafuchoco.mofu.mofueventassist.automation.ActionOptions;

/**
 * @param soundType     See below for a list of supported sounds.\n
 *                      https://papermc.io/javadocs/paper/1.17/org/bukkit/Sound.html
 * @param soundCategory See below for a list of supported sound categories.\n
 *                      https://papermc.io/javadocs/paper/1.17/org/bukkit/SoundCategory.html
 * @param location      Location to play sound. (Format: World, LocationX, LocationY, LocationZ)
 * @param volume        Volume of the sound to play.
 * @param pitch         Pitch of the sound to play.
 */
public record SoundPlayActionOptions(String soundType, String soundCategory,
                                     String location, String volume,
                                     String pitch) implements ActionOptions {

    public Sound getSoundType() {
        return Sound.valueOf(soundType);
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.valueOf(soundCategory);
    }

    public String getLocation() {
        return location;
    }

    public float getVolume() {
        return Float.parseFloat(volume);
    }

    public float getPitch() {
        return Float.parseFloat(pitch);
    }
}
