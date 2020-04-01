/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.login;

import net.neferett.webviewsinjector.login.steps.OneStep;
import net.neferett.webviewsinjector.login.steps.Step;
import net.neferett.webviewsinjector.login.steps.TwoStep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@AllArgsConstructor
@Getter
public enum StepEnum {

    /* Same Page Email and Password */
    ONE_STEP(OneStep.class, 1),
    /* Email and Password on different pages */
    TWO_STEP(TwoStep.class, 2);

    /* Enum attributes */
        private final Class<?> step;
        private final int multiplicand;
    /* end */

    /**
     *
     * @param stepEnum StepEnum to automatically instantiate
     * @return StepEnum
     */
    @SneakyThrows
    public static Step createInstance(StepEnum stepEnum) {
        return (Step) stepEnum.getStep().getDeclaredConstructors()[0].newInstance();
    }
}
