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
package org.ops4j.sisyphos.api.simulation;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Builds virtual users for a simulation.
 *
 * @author Harald Wellmann
 *
 */
public class UserBuilder {

    private static AtomicLong userId = new AtomicLong();

    private int numUsers;
    private Duration interval = Duration.ZERO;

    /**
     * Creates a builder for the given number of users.
     *
     * @param numUsers
     *            number of users
     */
    public UserBuilder(int numUsers) {
        this.numUsers = numUsers;
    }

    /**
     * Sets the interval between sessions to zero.
     * <p>
     * All sessions will be started at once.
     *
     * @return this builder
     */
    public UserBuilder atOnce() {
        this.interval = Duration.ZERO;
        return this;
    }

    /**
     * Sets the interval between sessions.
     * <p>
     * After starting a session, Sisyphos will wait for the given duration before starting the next
     * one.
     *
     * @return this builder
     */
    public UserBuilder atInterval(Duration interval) {
        this.interval = interval;
        return this;
    }

    /**
     * Gets the number of users, i.e. the number of sessions to be started.
     *
     * @return number of users
     */
    public int getNumUsers() {
        return numUsers;
    }

    /**
     * Gets the duration that Sisyphos will wait before starting the next session.
     *
     * @return interval between sessions
     */
    public Duration getInterval() {
        return interval;
    }

    public long nextUserId() {
        return userId.incrementAndGet();
    }
}
