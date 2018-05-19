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

/**
 * Builds a repeated action.
 * @author Harald Wellmann
 *
 */
public class RepeatBuilder implements ActionBuilder {

    private int numRepetitions;
    private ActionBuilder stepBuilder;

    RepeatBuilder(int numIterations, ActionBuilder stepBuilder) {
        this.numRepetitions = numIterations;
        this.stepBuilder = stepBuilder;
    }

    /**
     * Gets the number of repetitions.
     * @return number of repetitions
     */
    public int getNumRepetitions() {
        return numRepetitions;
    }

    /**
     * Gets the action to be repeated.
     * @return action
     */
    public ActionBuilder getStepBuilder() {
        return stepBuilder;
    }
}
