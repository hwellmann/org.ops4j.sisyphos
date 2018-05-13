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
import org.ops4j.sisyphos.api.user.UserBuilder;
import org.ops4j.spi.ServiceProviderFinder;

import io.vavr.collection.List;

/**
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

    SimulationBuilder(String name) {
        this.name = name;
        this.userBuilders = List.empty();
        this.scenarioBuilders = List.empty();
    }

    public SimulationBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public SimulationBuilder withRunDescription(String runDescription) {
        this.runDescription = runDescription;
        return this;
    }

    public SimulationBuilder withReportDir(String reportDir) {
        this.reportDir = reportDir;
        return this;
    }

    public SimulationBuilder withScenario(ScenarioBuilder scenarioBuilder) {
        this.scenarioBuilders = scenarioBuilders.append(scenarioBuilder);
        return this;
    }

    public SimulationBuilder withConfig(ProtocolConfiguration config) {
        this.protocolConfiguration = config;
        return this;
    }

    void appendUsers(UserBuilder userBuilder) {
        this.userBuilders = userBuilders.append(userBuilder);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        if (id == null) {
            id = name.toLowerCase().replaceAll("[^a-z0-9]", "_");
        }
        return id;
    }

    public String getRunDescription() {
        return runDescription;
    }

    public String getReportDir() {
        return reportDir;
    }

    public List<ScenarioBuilder> getScenarioBuilders() {
        return scenarioBuilders;
    }

    public ProtocolConfiguration getProtocolConfiguration() {
        return protocolConfiguration;
    }

    public void run() {
        SimulationRunner simulationRunner = ServiceProviderFinder.loadUniqueServiceProvider(SimulationRunner.class);
        simulationRunner.runSimulation(this);
    }
}
