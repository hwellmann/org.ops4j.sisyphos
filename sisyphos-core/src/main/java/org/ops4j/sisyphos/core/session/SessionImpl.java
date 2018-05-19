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

import static java.util.stream.Collectors.toList;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ops4j.sisyphos.api.action.Group;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.session.Status;

/**
 * @author Harald Wellmann
 *
 */
public class SessionImpl implements ExtendedSession {

    private String scenario;
    private Map<String, Object> attributes;
    private Long userId;
    private long startDate;
    private Status status = Status.OK;
    private ExpressionEngine engine;
    private Deque<Group> groupStack = new LinkedList<Group>();

    public SessionImpl(String scenario, Map<String, Object> attributes, long userId) {
        this.scenario = scenario;
        this.attributes = attributes;
        this.userId = userId;
        this.startDate = System.currentTimeMillis();
        this.engine = new ExpressionEngine(this);
    }

    public SessionImpl(String scenario, long userId) {
        this(scenario, new HashMap<>(), userId);
    }

    public SessionImpl(String scenario) {
        this(scenario, new HashMap<>(), 0);
    }

    public SessionImpl(long userId) {
        this("", new HashMap<>(), userId);
    }

    @Override
    public String getScenario() {
        return scenario;
    }

    @Override
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public long getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Session markAsFailed() {
        status = Status.KO;
        return this;
    }

    @Override
    public int compareTo(Session other) {
        return Long.compare(userId, other.getUserId());
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
        engine.setValue(key, value);
    }

    public ExpressionEngine getEngine() {
        return engine;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    @Override
    public <T> void setAttributes(Map<String, T> attributes) {
        attributes.forEach((k, v) -> setAttribute(k, v));

    }

    @Override
    public Group startGroup(String groupName) {
        Group group = new Group(groupName);
        if (groupStack.contains(group)) {
            throw new IllegalArgumentException(String.format("Group %s already exists", groupName));
        }
        groupStack.push(group);
        return group;
    }

    @Override
    public Group endGroup(String groupName) {
        if (groupStack.isEmpty() || !groupStack.peek().getName().equals(groupName)) {
            throw new IllegalArgumentException(String.format("Group on top of stack does not match %s", groupName));
        }
        Group top = groupStack.pop();
        if (!groupStack.isEmpty()) {
            groupStack.peek().accumulateResonseTime(top.getCumulatedResponseTime());
        }
        return top;
    }

    @Override
    public List<String> getGroupHierarchy() {
        return groupStack.stream().map(Group::getName).collect(toList());
    }

    @Override
    public void accumulateResponseTime(long responseTime) {
        if (groupStack.isEmpty()) {
            return;
        }
        groupStack.peek().accumulateResonseTime(responseTime);
    }

    @Override
    public String toString() {
        return "SessionImpl [scenario=" + scenario + ", userId=" + userId + ", attributes=" + attributes + "]";
    }
}
