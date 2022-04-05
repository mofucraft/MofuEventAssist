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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import page.nafuchoco.mofu.mofueventassist.automation.ActionOptions;
import page.nafuchoco.mofu.mofueventassist.automation.EventAutomationAction;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ItemGiveAction.class),
        @JsonSubTypes.Type(value = MessageSendAction.class),
        @JsonSubTypes.Type(value = SoundPlayAction.class),
        @JsonSubTypes.Type(value = TitleShowAction.class)
})
public abstract class AutomationAction implements EventAutomationAction {
    private final ActionOptions actionOptions;

    protected AutomationAction(ActionOptions actionOptions) {
        this.actionOptions = actionOptions;
    }

    @Override
    public ActionOptions getOptions() {
        return actionOptions;
    }
}
