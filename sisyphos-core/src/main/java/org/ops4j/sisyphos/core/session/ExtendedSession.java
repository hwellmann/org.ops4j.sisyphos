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
package org.ops4j.sisyphos.core.session;

import java.util.List;

import org.ops4j.sisyphos.api.action.Group;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.session.Status;

/**
 * Extended session interface with some additional methods that may be used by other Sisyphos
 * components, but not by user code.
 *
 * @author Harald Wellmann
 *
 */
public interface ExtendedSession extends Session {

    /**
     * Sets the scenario name.
     *
     * @param scenario
     *            scenario name
     */
    void setScenario(String scenario);

    /**
     * Sets the user identity
     *
     * @param userId
     *            user identity
     */
    void setUserId(long userId);

    /**
     * Sets the start timestamp
     *
     * @param startDate
     *            start timestamp
     */
    void setStartDate(long startDate);

    /**
     * Sets the status.
     *
     * @param status
     *            session status
     */
    void setStatus(Status status);

    /**
     * Starts the group with the given name and pushes it on the internal group stack.
     *
     * @param groupName
     *            group name
     * @return new group
     */
    Group startGroup(String groupName);

    /**
     * Ends the group with the given name.
     *
     * @param groupName
     *            group name
     * @return group popped from the stack
     */
    Group endGroup(String groupName);

    /**
     * Gets the current group names, outermost (oldest) first.
     *
     * @return list of group names, empty when no group is active
     */
    List<String> getGroupHierarchy();

    /**
     * Accumulates response time for the current group.
     *
     * @param responseTime
     *            response time to be added to the value accumulated so far
     */
    void accumulateResponseTime(long responseTime);
}
