/*
 * Copyright 2018 OPS4J Contributors
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
 * @author Harald Wellmann
 *
 */
public class GroupMessage implements StatisticsMessage {

    private String scenario;
    private long userId;
    private List<String> groups;
    private long startTimestamp;
    private long endTimestamp;
    private long cumulatedResponseTime;
    private Status status;

    public GroupMessage(String scenario, long userId, List<String> groups,
        long startTimestamp, long endTimestamp, long cumulatedResponseTime, Status status) {
        this.scenario = scenario;
        this.userId = userId;
        this.groups = groups;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.cumulatedResponseTime = cumulatedResponseTime;
        this.status = status;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public long getCumulatedResponseTime() {
        return cumulatedResponseTime;
    }

    public void setCumulatedResponseTime(long cumulatedResponseTime) {
        this.cumulatedResponseTime = cumulatedResponseTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
