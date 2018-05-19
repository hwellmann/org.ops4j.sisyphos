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
 * Builds an action that will be retried on failure.
 *
 * @author Harald Wellmann
 *
 */
public class RetryBuilder implements ActionBuilder {

    private int numRetries;
    private ActionBuilder stepBuilder;

    RetryBuilder(int numRetries, ActionBuilder stepBuilder) {
        this.numRetries = numRetries;
        this.stepBuilder = stepBuilder;
    }

    /**
     * Gets the maximum number of retries.
     *
     * @return number of retries
     */
    public int getNumRetries() {
        return numRetries;
    }

    /**
     * Gets the action to be executed, possibly retrying after failure.
     *
     * @return action
     */
    public ActionBuilder getStepBuilder() {
        return stepBuilder;
    }
}
