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
package org.ops4j.sisyphos.core.runner;

import static org.ops4j.sisyphos.api.simulation.Sisyphos.atOnce;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.exec;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.request;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.scenario;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.simulation;

import org.junit.Test;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.simulation.ScenarioBuilder;

/**
 * @author Harald Wellmann
 *
 */
public class DslTest {

    private Session actionWithRetry(Session session) {
        Integer numTries = session.getAttribute("numTries", Integer.class);
        if (numTries == null) {
            session.setAttribute("numTries", 1);
            throw new IllegalStateException("forced retry");
        }
        session.setAttribute("numTries", numTries + 1);
        return session;
    }

    @Test
    public void shouldRetry() {
        ScenarioBuilder scenario = scenario("Retrying")
            .retry(1, exec(request("Retrying", this::actionWithRetry)));

        simulation("test")
        .withScenario(scenario
            .withUsers(atOnce(1)))
        .run();
    }
}
