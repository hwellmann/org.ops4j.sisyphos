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
 * @author Harald Wellmann
 *
 */
public class SimulationMessage implements StatisticsMessage {

    private String simulationName;
    private String simulationId;
    private long startTime;
    private String runDescription;
    private String reportDir;

    public SimulationMessage(String simulationName, String simulationId, long startTime,
        String runDescription, String reportDir) {
        this.simulationName = simulationName;
        this.simulationId = simulationId;
        this.startTime = startTime;
        this.runDescription = runDescription;
        this.reportDir = reportDir;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getSimulationId() {
        return simulationId;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getRunDescription() {
        return runDescription;
    }

    public String getReportDir() {
        return reportDir;
    }
}
