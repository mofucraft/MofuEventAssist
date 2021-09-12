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

import lombok.Data;
import page.nafuchoco.mofu.mofueventassist.automation.EventAutomation;

@Data
public class EventOptions {
    /*
    private boolean enableStartAnnounce;
    private boolean enableStartCountdown;
    private boolean enableAutoDelete;
    */
    
    private EventAutomation startAutomation;
    private EventAutomation endAutomation;
}
