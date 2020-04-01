/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.login.steps;

import net.neferett.webviewsinjector.javascript.ElementValue;
import net.neferett.webviewsinjector.javascript.JSInjector;
import net.neferett.webviewsinjector.login.TypesAuthElement;
import lombok.Data;

import java.util.HashMap;

@Data
public abstract class Step {

    /**
     * Arg Constructor
     */
    private final JSInjector jsInjector;

    private HashMap<TypesAuthElement, ElementValue> elementElementValueHashMap = new HashMap<>();

    Step() {
        this.jsInjector = new JSInjector();
    }

    /**
     *
     * @param element TypesAuth to set
     * @param value Value instance with javascript method and identifier
     */
    public void addElement(TypesAuthElement element, ElementValue value) {
        this.elementElementValueHashMap.put(element, value);
    }

    /**
     *
     * @param input TypeAuth for input purpose
     * @param button TypeAuth for button click purpose
     * @param data Data to put inside input
     * @param delay Delay between this and next action
     * @return String - Create multiple step promise
     * @throws Exception Throwing when TypesAuthElement and ElementValue are not linked
     */
    String stepPromise(TypesAuthElement input, TypesAuthElement button, String data, int delay) throws Exception {
        ElementValue inputValue = this.elementElementValueHashMap.get(input);

        if (inputValue == null)
            throw new Exception(input.getName() + " element doesn't exist");

        if (button == null)
            return this.jsInjector.promiseCreator(delay, inputValue.changeValue(data));

        ElementValue buttonValue = this.elementElementValueHashMap.get(button);

        if (buttonValue == null)
            throw new Exception(input.getName() + " element doesn't exist");

        return this.jsInjector.promiseCreator(delay, inputValue.changeValue(data), buttonValue.clickValue());
    }

    /**
     *
     * @param element TypeAuth element to get
     * @return ElementValue by Type Auth
     * @throws Exception Throwing when TypesAuthElement and ElementValue are not linked
     */
    ElementValue getElementByTypes(TypesAuthElement element) throws Exception {
        ElementValue value = this.elementElementValueHashMap.get(element);

        if (value == null)
            throw new Exception(element.getName() + " doesn't exist");

        return value;
    }

    /**
     *
     * Promise Creator
     *
     * @param username Username or Email to fill in Login form
     * @param password Password to fill in Login form
     * @param delay Delay between fill and connection click button
     * @return String - Promise to inject with delay
     * @throws Exception Throwing when element is not defined
     */
    public abstract String construct(String username, String password, int delay) throws Exception;
}
