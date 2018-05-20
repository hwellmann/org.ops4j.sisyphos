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
package org.ops4j.sisyphos.api.simulation;

import org.ops4j.sisyphos.api.action.ProtocolConfiguration;
import org.ops4j.spi.ServiceProviderFinder;

import io.vavr.collection.List;

/**
 * Builds a simulation in fluent syntax.
 *
 * @author Harald Wellmann
 *
 */
public class SimulationBuilder {

    private String name;
    private String id;
    private String runDescription;
    private String reportDir;
    private List<UserBuilder> userBuilders;
    private List<ScenarioBuilder> scenarioBuilders;
    private ProtocolConfiguration protocolConfiguration;

    /**
     * Creates a builder for a simulation with the given name.
     *
     * @param name
     *            simulation name
     */
    SimulationBuilder(String name) {
        this.name = name;
        this.userBuilders = List.empty();
        this.scenarioBuilders = List.empty();
    }

    /**
     * Sets the run identity for this simulation.
     * <p>
     * TODO Do we really need this?
     *
     * @param id
     *            run identity
     * @return this builder
     */
    public SimulationBuilder withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the run description for this simulation.
     *
     * @param runDescription
     *            run description
     * @return this builder
     */
    public SimulationBuilder withRunDescription(String runDescription) {
        this.runDescription = runDescription;
        return this;
    }

    /**
     * Sets the report directory for this simulation. Any reports for the simulation run will be
     * created in this directory.
     *
     * @param reportDir
     *            report directory
     * @return this builder
     */
    public SimulationBuilder withReportDir(String reportDir) {
        this.reportDir = reportDir;
        return this;
    }

    /**
     * Sets a scenario to be run in this simulation.
     *
     * @param scenarioBuilder
     *            scenario builder
     * @return this builder
     */
    public SimulationBuilder withScenario(ScenarioBuilder scenarioBuilder) {
        this.scenarioBuilders = scenarioBuilders.append(scenarioBuilder);
        return this;
    }

    /**
     * Sets a protocol configuration for a given protocol (e.g. HTTP) to be used in this simulation
     *
     * @param config
     *            protocol configuration
     * @return this builder
     */
    public SimulationBuilder withConfig(ProtocolConfiguration config) {
        this.protocolConfiguration = config;
        return this;
    }

    void appendUsers(UserBuilder userBuilder) {
        this.userBuilders = userBuilders.append(userBuilder);
    }

    /**
     * Gets the name of this simulation.
     *
     * @return simulation name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the run identity of this simulation.
     *
     * @return run identity
     */
    public String getId() {
        if (id == null) {
            id = name.toLowerCase().replaceAll("[^a-z0-9]", "_");
        }
        return id;
    }

    /**
     * Gets the run description of this simulation.
     *
     * @return run description
     */
    public String getRunDescription() {
        return runDescription;
    }

    /**
     * Gets the report directory.
     *
     * @return report directory
     */
    public String getReportDir() {
        return reportDir;
    }

    /**
     * Gets the scenarios of this simulation.
     *
     * @return list of scenario builders
     */
    public List<ScenarioBuilder> getScenarioBuilders() {
        return scenarioBuilders;
    }

    /**
     * Gets the protocol configuration of this simulation.
     *
     * @return protocol configuration (or null)
     */
    public ProtocolConfiguration getProtocolConfiguration() {
        return protocolConfiguration;
    }

    /**
     * Runs this simulation. This method blocks until all sessions of the simulation have
     * terminated. A simulation logfile will be created in the report directory.
     */
    public void run() {
        SimulationRunner simulationRunner = ServiceProviderFinder
            .loadUniqueServiceProvider(SimulationRunner.class);
        simulationRunner.runSimulation(this);
    }
}
