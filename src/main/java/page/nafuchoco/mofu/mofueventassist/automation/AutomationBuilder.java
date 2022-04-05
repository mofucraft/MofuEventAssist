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

package page.nafuchoco.mofu.mofueventassist.automation;

import page.nafuchoco.mofu.mofueventassist.automation.actions.*;
import page.nafuchoco.mofu.mofueventassist.element.GameEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AutomationBuilder {
    private final GameEvent gameEvent;

    private final List<AutomationAction> actions;
    private int actionDelayTime = 1;

    public AutomationBuilder(GameEvent gameEvent) {
        this.gameEvent = gameEvent;
        actions = new ArrayList<>();
    }

    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public EventAutomation build() {
        return new EventAutomation(actions, actionDelayTime);
    }


    public AutomationBuilder addAutomationAction(AutomationAction automationAction) {
        actions.add(automationAction);
        return this;
    }

    public AutomationBuilder setActionDelayTime(int actionDelayTime) {
        this.actionDelayTime = actionDelayTime;
        return this;
    }

    public List<AutomationAction> getActions() {
        return Collections.unmodifiableList(actions);
    }


    public static class AutomationActionBuilder {
        private final GameEvent gameEvent;

        private AutomationActionType automationActionType;
        private Map<String, Object> automationActionOptionsMap;

        public AutomationActionBuilder(GameEvent gameEvent) {
            this.gameEvent = gameEvent;
        }

        public GameEvent getGameEvent() {
            return gameEvent;
        }

        public AutomationAction build() {
            if (!canBuild())
                throw new IllegalStateException("The configuration required for the build has not been completed.");

            AutomationAction eventAutomationAction;
            ActionOptions actionOptions;

            try {
                // フィールドの数でコンストラクタを判定、呼び出し
                actionOptions = (ActionOptions) automationActionType.getActionOptionsClass().getDeclaredConstructor(
                        Arrays.stream(automationActionType.getActionOptionsClass().getDeclaredFields())
                                .map(Field::getType)
                                .toArray(Class[]::new)
                ).newInstance(automationActionOptionsMap.values().toArray()); // Classからインスタンスの生成
                eventAutomationAction = (AutomationAction) automationActionType.getActionClass().getDeclaredConstructor(
                        GameEvent.class, ActionOptions.class
                ).newInstance(gameEvent, actionOptions);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }

            return eventAutomationAction;
        }

        public boolean canBuild() {
            if (automationActionOptionsMap != null && automationActionOptionsMap.values().stream().noneMatch(Objects::isNull))
                return true;
            return false;
        }


        public AutomationActionBuilder setEventAutomationActionType(AutomationActionType actionType) {
            automationActionType = actionType;
            return this;
        }

        public AutomationActionBuilder setAutomationActionOption(String field, String value) {
            if (automationActionOptionsMap == null) {
                automationActionOptionsMap = new LinkedHashMap<>();
                getOptionFields().forEach(f -> automationActionOptionsMap.put(f, null));
            }

            if (automationActionOptionsMap.containsKey(field))
                automationActionOptionsMap.put(field, value);
            return this;
        }


        public List<String> getOptionFields() {
            var fields = new ArrayList<String>();
            for (Field field : automationActionType.getActionOptionsClass().getDeclaredFields())
                fields.add(field.getName());
            return fields;
        }

        private String getTypeName(Class type) {
            if (!type.isArray())
                return type.getName();
            return getTypeName(type.getComponentType()) + "[]";
        }
    }


    public enum AutomationActionType {
        SEND_MESSAGE(MessageSendAction.class, MessageSendActionOptions.class),
        SHOW_TITLE(TitleShowAction.class, TitleShowActionOptions.class),
        PLAY_SOUND(SoundPlayAction.class, SoundPlayActionOptions.class),
        GIVE_ITEM(ItemGiveAction.class, ItemGiveActionOptions.class);
        // TELEPORT(actionClass, actionOptionsClass);

        private final Class actionClass;
        private final Class actionOptionsClass;

        AutomationActionType(Class actionClass, Class actionOptionsClass) {
            this.actionClass = actionClass;
            this.actionOptionsClass = actionOptionsClass;
        }

        public Class getActionClass() {
            return actionClass;
        }

        public Class getActionOptionsClass() {
            return actionOptionsClass;
        }
    }
}
