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

/**
 * Message emitted when starting a simulation run.
 *
 * @author Harald Wellmann
 *
 */
public class RunMessage implements StatisticsMessage {

    private static final String VERSION = "2.0";

    private String simulationName;
    private String userDefinedSimulationId;
    private String defaultSimulationId;
    private long startTime;
    private String runDescription;

    /**
     * Creates a run message with the given properties.
     *
     * @param simulationName
     *            unique name under which the simulation is registered
     * @param userDefinedSimulationId
     *            user defined name for this run, used as report header
     * @param defaultSimulationId
     *            fallback name when user defined name is not set
     * @param startTime
     *            start time of this run
     * @param runDescription
     *            run description, appearing in secondary header
     */
    public RunMessage(String simulationName, String userDefinedSimulationId,
        String defaultSimulationId, long startTime, String runDescription) {
        this.simulationName = simulationName;
        this.userDefinedSimulationId = userDefinedSimulationId;
        this.defaultSimulationId = defaultSimulationId;
        this.startTime = startTime;
        this.runDescription = runDescription;
    }

    /**
     * Gets the name under which the simulation is registered. This name can be used for remote
     * execution.
     *
     * @return simulation name
     */
    public String getSimulationName() {
        return simulationName;
    }

    /**
     * Gets the user-defined simulation identity which may override the default identity for this
     * run. This will be the heading of the generated report.
     *
     * @return user-defined simulation identity
     */
    public String getUserDefinedSimulationId() {
        return userDefinedSimulationId;
    }

    /**
     * Gets the start time of this run.
     *
     * @return start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets the run description, which will appear in the secondary heading of the generated report.
     *
     * @return run description
     */
    public String getRunDescription() {
        return runDescription;
    }

    /**
     * Gets the default simulation identity to be used when there is no user-defined identity.
     * @return default simulation identity
     */
    public String getDefaultSimulationId() {
        return defaultSimulationId;
    }

    /**
     * Gets the record format version.
     * @return format version
     */
    public String getVersion() {
        return VERSION;
    }
}
