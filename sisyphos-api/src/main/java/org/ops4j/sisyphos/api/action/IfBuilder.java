/*
 * Copyright 2017 OPS4J Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.sisyphos.api.action;

import java.util.function.Predicate;

import org.ops4j.sisyphos.api.session.Session;

/**
 * Builds an either-or-action.
 *
 * @author Harald Wellmann
 *
 */
public class IfBuilder implements ActionBuilder {

    private Predicate<Session> condition;
    private ActionBuilder ifAction;
    private ActionBuilder elseAction;

    IfBuilder(Predicate<Session> condition, ActionBuilder ifAction, ActionBuilder elseAction) {
        this.condition = condition;
        this.ifAction = ifAction;
        this.elseAction = elseAction;
    }

    /**
     * Gets the condition or predicate to be checked on the current session.
     * @return condition
     */
    public Predicate<Session> getCondition() {
        return condition;
    }

    /**
     * Gets the action to be executed if the condition is true.
     * @return if-action
     */
    public ActionBuilder getIfAction() {
        return ifAction;
    }

    /**
     * Gets the action to be executed if the condition is false.
     * @return else-action
     */
    public ActionBuilder getElseAction() {
        return elseAction;
    }
}
