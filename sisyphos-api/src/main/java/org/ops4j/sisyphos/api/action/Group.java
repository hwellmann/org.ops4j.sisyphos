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
package org.ops4j.sisyphos.api.action;

/**
 * A group gives a name to a list of related actions and tracks the cumulated response time.
 *
 * @author hwellmann
 *
 */
public class Group {

    private String name;
    private long startDate;
    private long endDate;
    private long cumulatedResponseTime;

    /**
     * Creates a group with the given name.
     * @param name group name
     */
    public Group(String name) {
        this.name = name;
        this.startDate = System.currentTimeMillis();
    }

    /**
     * Gets the name.
     * @return group name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param name group name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the start timestamp.
     * @return start of group execution
     */
    public long getStartDate() {
        return startDate;
    }

    /**
     * Sets the start timestamp.
     * @param startDate start of group execution
     */
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end timestamp.
     * @return end of group execution
     */
    public long getEndDate() {
        return endDate;
    }

    /**
     * Sets the end timestamp
     * @param endDate end of group execution
     */
    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    /**
     * Add the given time to the accumulated response time.
     * @param responseTime response time in ms
     */
    public void accumulateResponseTime(long responseTime) {
        this.cumulatedResponseTime += responseTime;
    }

    /**
     * Gets the cumulated response time in ms.
     * @return response time
     */
    public long getCumulatedResponseTime() {
        return cumulatedResponseTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Group)) {
            return false;
        }
        Group other = (Group) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
