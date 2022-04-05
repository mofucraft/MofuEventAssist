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

package page.nafuchoco.mofu.mofueventassist.editor;

import page.nafuchoco.mofu.mofueventassist.automation.AutomationBuilder;
import page.nafuchoco.mofu.mofueventassist.editor.actions.EditorAction;
import page.nafuchoco.mofu.mofueventassist.element.GameEvent;
import page.nafuchoco.mofu.mofueventassist.element.GameEventBuilder;

public class EventEditor {
    private final GameEventBuilder builder;

    private EditorAction waitingAction;

    public EventEditor() {
        builder = new GameEventBuilder();
    }

    public GameEventBuilder getBuilder() {
        return builder;
    }

    public EditorAction getWaitingAction() {
        return waitingAction;
    }

    public void setWaitingAction(EditorAction waitingAction) {
        this.waitingAction = waitingAction;
    }


    // ここから下はアクション設定用
    private GameEvent builtEvent;
    private int automationType = -1; // -1: not selected, 0: start, 1: end
    private AutomationBuilder automationBuilder;
    private AutomationBuilder.AutomationActionBuilder actionBuilder;

    public GameEvent getBuiltEvent() {
        return builtEvent;
    }

    public void setBuiltEvent(GameEvent builtEvent) {
        this.builtEvent = builtEvent;
    }

    public int getAutomationType() {
        return automationType;
    }

    public void setAutomationType(int automationType) {
        this.automationType = automationType;
    }

    public AutomationBuilder getAutomationBuilder() {
        return automationBuilder;
    }

    public void setAutomationBuilder(AutomationBuilder automationBuilder) {
        this.automationBuilder = automationBuilder;
    }

    public AutomationBuilder.AutomationActionBuilder getActionBuilder() {
        return actionBuilder;
    }

    public void setActionBuilder(AutomationBuilder.AutomationActionBuilder actionBuilder) {
        this.actionBuilder = actionBuilder;
    }
}
