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
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import page.nafuchoco.mofu.mofueventassist.MofuEventAssist;
import page.nafuchoco.mofu.mofueventassist.automation.ActionOptions;
import page.nafuchoco.mofu.mofueventassist.automation.AutomationActionContext;

import java.util.Objects;

public class ItemGiveAction extends AutomationAction {

    public ItemGiveAction(@JsonProperty("options") ActionOptions actionOptions) {
        super(actionOptions);
    }

    @Override
    public void execute(AutomationActionContext context) {
        ItemGiveActionOptions options = (ItemGiveActionOptions) getOptions();
        ItemStack itemStack = new ItemStack(Material.getMaterial(options.materialName()));
        itemStack.setAmount(NumberUtils.toInt(options.amount(), 1));
        context.gameEvent().getEntrant().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(p -> Bukkit.getServer().getScheduler().runTask(MofuEventAssist.getInstance(), () -> p.getInventory().addItem(itemStack)));
    }
}
