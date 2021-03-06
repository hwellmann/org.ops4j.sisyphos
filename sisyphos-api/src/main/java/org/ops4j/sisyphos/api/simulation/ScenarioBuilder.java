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

import org.ops4j.sisyphos.api.action.ActionBuilder;
import org.ops4j.sisyphos.api.action.Exec;

import io.vavr.collection.List;

/**
 * Builds a scenarion of a simulation in fluent syntax.
 *
 * @author Harald Wellmann
 *
 */
public class ScenarioBuilder implements Exec<ScenarioBuilder> {

    private String name;
    private List<UserBuilder> userBuilders;
    private List<ActionBuilder> actionBuilders;


    ScenarioBuilder(String name) {
        this(name, List.empty());
    }

    ScenarioBuilder(String name, List<ActionBuilder> actionBuilders) {
        this(name, List.empty(), actionBuilders);
    }

    ScenarioBuilder(String name, List<UserBuilder> userBuilders, List<ActionBuilder> actionBuilders) {
        this.name = name;
        this.userBuilders = userBuilders;
        this.actionBuilders = actionBuilders;
    }


    /**
     * Gets the name of this scenario.
     * @return scenario name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets user builder for this scenario.
     * @param userBuilders user builders
     * @return this builer
     */
    public ScenarioBuilder with(UserBuilder... userBuilders) {
        this.userBuilders = this.userBuilders.appendAll(List.of(userBuilders));
        return this;
    }

    /**
     * Gets the user builders for this scenario.
     * @return user builders
     */
    public List<UserBuilder> getUserBuilders() {
        return userBuilders;
    }

    @Override
    public ScenarioBuilder newInstance(List<ActionBuilder> actionBuilders) {
        return new ScenarioBuilder(name, userBuilders, actionBuilders);
    }

    @Override
    public List<ActionBuilder> actionBuilders() {
        return actionBuilders;
    }
}
