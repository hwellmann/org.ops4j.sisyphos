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
package org.ops4j.sisyphos.api.simulation;

import java.util.List;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;

/**
 * Registry for all simulations currently defined in the system.
 * <p>
 * Used in distributed mode to run a simulation by name on a given worker node.
 *
 * @author Harald Wellmann
 */
public final class SimulationRegistry {

    private static Map<String, SimulationBuilder> simulationMap = HashMap.empty();


    private SimulationRegistry() {
        // hidden constructor
    }

    /**
     * Registers a simulation with the given name.
     * @param simulation simulation
     */
    public static void register(SimulationBuilder simulation) {
        String name = simulation.getName();
        if (simulationMap.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Simulation named '%s' already exists", name));
        }
        simulationMap = simulationMap.put(name, simulation);
    }

    /**
     * Finds a simulation with the given name.
     * @param name simulation name
     * @return matching simulation, if present
     */
    public static Option<SimulationBuilder> find(String name) {
        return simulationMap.get(name);
    }

    /**
     * Finds all names of simulation stored in this registry.
     * @return sorted list of names
     */
    public static List<String> findNames() {
        return simulationMap.keySet().toSortedSet().toJavaList();
    }
}
