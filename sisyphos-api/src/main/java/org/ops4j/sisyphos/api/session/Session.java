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
package org.ops4j.sisyphos.api.session;

import java.util.Map;

/**
 * A session holds the state of a scenario executed by a user.
 * <p>
 * A session may hold user-defined attributes which are mutable.
 *
 * @author Harald Wellmann
 *
 */
public interface Session extends Comparable<Session> {

    /**
     * Gets the name of the scenario being executed.
     *
     * @return scenarion name
     */
    String getScenario();

    /**
     * Gets the user identity of the user running this session.
     *
     * @return user identity
     */
    Long getUserId();

    /**
     * Gets the start date of this session.
     *
     * @return start date
     */
    long getStartDate();

    /**
     * Gets the status of this session.
     *
     * @return status
     */
    Status getStatus();

    /**
     * Marks the session as failed.
     */
    void markAsFailed();

    /**
     * Sets an attribute on this session.
     *
     * @param key
     *            attribute name
     * @param value
     *            attribute value
     * @param <T>
     *            attribute value type
     */
    <T> void setAttribute(String key, T value);

    /**
     * Sets multiple attributes on this session. This is shorthand for iterating over the given map
     * entries and calling {@link #setAttribute(String, Object)} for each entry.
     *
     * @param attributes
     *            map of attributes
     * @param <T>
     *            attribute value type
     */
    <T> void setAttributes(Map<String, T> attributes);

    /**
     * Gets the attibute value for the given attribute name.
     *
     * @param key
     *            attribute name
     * @param <T>
     *            attribute value type
     * @return attribute value or null
     */
    <T> T getAttribute(String key);
}
