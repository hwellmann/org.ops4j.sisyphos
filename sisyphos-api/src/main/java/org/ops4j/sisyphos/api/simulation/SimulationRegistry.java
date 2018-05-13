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

public class SimulationRegistry {

    private static Map<String, SimulationBuilder> simulationMap = HashMap.empty();

    public static void register(String name, SimulationBuilder simulation) {
        if (simulationMap.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Simulation named '%s' already exists", name));
        }
        simulationMap = simulationMap.put(name, simulation);
    }

    public static Option<SimulationBuilder> find(String name) {
        return simulationMap.get(name);
    }

    public static List<String> findNames() {
        return simulationMap.keySet().toSortedSet().toJavaList();
    }
}
