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
package org.ops4j.sisyphos.core.message;

import java.util.List;

import org.ops4j.sisyphos.api.session.Status;

/**
 * Message emitted after completion of a request.
 *
 * @author Harald Wellmann
 *
 */
public class RequestMessage implements StatisticsMessage {

    private String scenario;
    private long userId;
    private List<String> groups;
    private String name;
    private long requestTimestamp;
    private long responseTimestamp;
    private Status status;
    private String message;

    /**
     * Constructs a request message with the given properties.
     *
     * @param scenario
     *            scenario name
     * @param userId
     *            user identity
     * @param groups
     *            group hierarchy of this request
     * @param name
     *            request name
     * @param requestTimestamp
     *            timestamp of request
     * @param responseTimestamp
     *            timestamp of response
     * @param status
     *            session status
     */
    public RequestMessage(String scenario, long userId, List<String> groups, String name,
        long requestTimestamp, long responseTimestamp, Status status) {
        this.scenario = scenario;
        this.userId = userId;
        this.groups = groups;
        this.name = name;
        this.requestTimestamp = requestTimestamp;
        this.responseTimestamp = responseTimestamp;
        this.status = status;
    }

    /**
     * Gets the scenario name.
     * @return scenario name
     */
    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    /**
     * Gets the user identity.
     * @return user identity
     */
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Gets the group hierarchy (from outer to inner).
     * @return list of group names
     */
    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    /**
     * Gets the name of this request.
     * @return request name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the timestamp taken just before sending the request.
     * @return request timestamp
     */
    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    /**
     * Gets the timestamp taken just after receiving the response.
     * @return response timestamp
     */
    public long getResponseTimestamp() {
        return responseTimestamp;
    }

    public void setResponseTimestamp(long responseTimestamp) {
        this.responseTimestamp = responseTimestamp;
    }

    /**
     * Gets the session status.
     * @return session status
     */
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Gets an error message, if the session is failed.
     * @return error message
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
