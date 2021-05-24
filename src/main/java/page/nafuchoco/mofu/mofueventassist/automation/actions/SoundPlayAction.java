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

import org.bukkit.Bukkit;
import page.nafuchoco.mofu.mofueventassist.automation.ActionOptions;
import page.nafuchoco.mofu.mofueventassist.element.GameEvent;

import java.util.Objects;

public class SoundPlayAction extends AutomationAction {

    public SoundPlayAction(GameEvent gameEvent, ActionOptions actionOptions) {
        super(gameEvent, actionOptions);
    }

    @Override
    public void execute() { // 参加者全員に指定されたサウンドを再生する。
        SoundPlayActionOptions options = (SoundPlayActionOptions) getOptions();
        getEvent().getEntrant().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(p -> p.playSound(
                        options.getLocation(),
                        options.getSoundType(),
                        options.getSoundCategory(),
                        options.getVolume(),
                        options.getPitch()));
    }
}
