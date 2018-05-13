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
package org.ops4j.sisyphos.http.json;

import static org.ops4j.sisyphos.api.simulation.Sisyphos.atInterval;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.exec;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.scenario;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.simulation;
import static org.ops4j.sisyphos.http.api.Dsl.http;
import static org.ops4j.sisyphos.http.api.Dsl.httpConfig;
import static org.ops4j.sisyphos.http.api.Dsl.raw;
import static org.ops4j.sisyphos.http.api.Dsl.status;

import java.time.Duration;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.ops4j.sisyphos.http.api.HttpConfiguration;
import org.ops4j.sisyphos.http.api.HttpRequestBuilder;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author Harald Wellmann
 *
 */
public class JsonTest {

    private HttpConfiguration httpConfig;
    private HttpRequestBuilder listPosts;
    private HttpRequestBuilder createPost;

    @Before
    public void before() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        httpConfig = httpConfig()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .acceptHeader(MediaType.APPLICATION_JSON)
                .contentTypeHeader(MediaType.APPLICATION_JSON);

        listPosts = http("List all posts")
            .get("posts")
            .check(status().is(200));

        createPost = http("Create post")
            .post("posts")
            .body(raw("{\"title\":\"New post\", \"body\": \"bla\", \"userId\": 17}"))
            .check(status().is(201));

    }

    @Test
    public void shouldBuildSimulation() {
        simulation("jsonplaceholder")
            .withConfig(httpConfig)
            .withScenario(scenario("jsontest")
                .withUsers(atInterval(20, Duration.ofMillis(10)))
                .repeat(3,
                    exec(listPosts)
                    .pause(1, 3)
                    .exec(createPost)
                    .pause(1, 3)
                    ))
            .run();
    }
}
