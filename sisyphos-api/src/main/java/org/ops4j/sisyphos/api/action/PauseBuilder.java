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

import java.time.Duration;

/**
 * Builds a pause action which does nothing for a given (random) duration.
 * @author Harald Wellmann
 *
 */
public class PauseBuilder implements ActionBuilder {

    private Duration minDuration;
    private Duration maxDuration;

    PauseBuilder(Duration minDuration, Duration maxDuration) {
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
    }

    /**
     * Gets minimum duration of random pause.
     * @return duration
     */
    public Duration getMinDuration() {
        return minDuration;
    }

    /**
     * Gets maximum duration of random pause.
     * @return duration
     */
    public Duration getMaxDuration() {
        return maxDuration;
    }
}
