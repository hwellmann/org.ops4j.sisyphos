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

import java.util.List;
import java.util.Map;

import org.ops4j.sisyphos.api.action.Group;

/**
 * @author Harald Wellmann
 *
 */
public interface Session extends Comparable<Session> {

    String getScenario();

    void setScenario(String scenario);

    Long getUserId();

    void setUserId(Long userId);

    long getStartDate();

    void setStartDate(long startDate);

    Status getStatus();

    void setStatus(Status status);

    Session markAsFailed();

    void setAttribute(String key, Object value);

    <T> void setAttributes(Map<String, T> attributes);

    Object getAttribute(String key);

    <T> T getAttribute(String key, Class<T> klass);

    Group startGroup(String groupName);
    Group endGroup(String groupName);
    List<String> getGroupHierarchy();
    void accumulateResponseTime(long responseTime);

}