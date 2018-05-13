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
package org.ops4j.sisyphos.http;

import static org.ops4j.sisyphos.api.simulation.Sisyphos.atInterval;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.exec;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.scenario;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.simulation;
import static org.ops4j.sisyphos.http.api.Dsl.http;
import static org.ops4j.sisyphos.http.api.Dsl.httpConfig;

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;
import org.ops4j.sisyphos.api.action.ChainBuilder;
import org.ops4j.sisyphos.http.api.HttpConfiguration;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author Harald Wellmann
 *
 */
public class AdvancedSimulation02Test {

    private HttpConfiguration httpConfig;
    private ChainBuilder search;
    private ChainBuilder browse;
    private ChainBuilder edit;

    @Before
    public void before() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        httpConfig = httpConfig()
                .baseUrl("http://computer-database.gatling.io")
                .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .acceptLanguageHeader("en-US,en;q=0.5")
                .acceptEncodingHeader("gzip, deflate")
                .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0");

        search = exec(http("Home")
                .get("/"))
            .pause(1)
            .exec(http("Search")
                .get("/computers?f=macbook"))
            .pause(1)
            .exec(http("Select")
                .get("/computers/6"))
            .pause(1);

        browse = exec(http("Home")
                .get("/"))
            .pause(2)
            .exec(http("Page 1")
                .get("/computers?p=1"))
            .pause(Duration.ofMillis(670))
            .exec(http("Page 2")
                .get("/computers?p=2"))
            .pause(Duration.ofMillis(629))
            .exec(http("Page 3")
                .get("/computers?p=3"))
            .pause(Duration.ofMillis(734))
            .exec(http("Page 4")
                .get("/computers?p=4"))
            .pause(5);

        edit = exec(http("Form")
            .get("/computers/new"))
        .pause(1)
        .exec(http("Post")
            .post("/computers")
            .formParam("name", "Beautiful Computer")
            .formParam("introduced", "2012-05-30")
            .formParam("discontinued", "")
            .formParam("company", "37"));
    }

    @Test
    public void shouldBuildSimulation() {
        simulation("Advanced Simulation 02")
            .withConfig(httpConfig)
            .withScenario(scenario("Users")
                .withUsers(atInterval(10, Duration.ofSeconds(1)))
                .exec(search, browse))
            .withScenario(scenario("Admins")
                .withUsers(atInterval(2, Duration.ofSeconds(5)))
                .exec(search, browse, edit))
            .run();
    }
}
