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

import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public SessionImpl(String scenario, long userId, Map<String, Object> attributes) {
        this.scenario = scenario;
        this.attributes = attributes;
        this.userId = userId;
        this.startDate = System.currentTimeMillis();
        this.engine = new ExpressionEngine(this);
    }

    public SessionImpl(String scenario, long userId) {
        this(scenario, userId, new HashMap<>());
    }

    public SessionImpl(String scenario) {
        this(scenario, 0, new HashMap<>());
    }

    public SessionImpl(long userId) {
        this("", userId, new HashMap<>());
    }

    @Override
    public String getScenario() {
        return scenario;
    }

    @Override
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public Map<String, ?> getAttributes() {
        return attributes;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
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
    public void markAsFailed() {
        status = Status.KO;
    }

    @Override
    public int compareTo(Session other) {
        return Long.compare(userId, other.getUserId());
    }

    @Override
    public <T> void setAttribute(String key, T value) {
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
        groupStack.add(group);
        return group;
    }

    @Override
    public Group endGroup(String groupName) {
        if (groupStack.isEmpty() || !groupStack.peekLast().getName().equals(groupName)) {
            throw new IllegalArgumentException(String.format("Group on top of stack does not match %s", groupName));
        }
        Group top = groupStack.pollLast();
        if (!groupStack.isEmpty()) {
            groupStack.peekLast().accumulateResponseTime(top.getCumulatedResponseTime());
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
        groupStack.peekLast().accumulateResponseTime(responseTime);
    }

    @Override
    public String toString() {
        return "SessionImpl [scenario=" + scenario + ", userId=" + userId + ", attributes=" + attributes + "]";
    }

    @Override
    public Set<String> getAttributeKeys() {
        return Collections.unmodifiableSet(attributes.keySet());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T removeAttribute(String key) {
        return (T) attributes.remove(key);
    }
}
