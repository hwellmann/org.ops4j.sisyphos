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
package org.ops4j.sisyphos.remote.simulation;

import static org.ops4j.sisyphos.api.simulation.Sisyphos.atInterval;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.exec;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.scenario;
import static org.ops4j.sisyphos.api.simulation.Sisyphos.simulation;
import static org.ops4j.sisyphos.http.api.Dsl.http;
import static org.ops4j.sisyphos.http.api.Dsl.httpConfig;
import static org.ops4j.sisyphos.http.api.Dsl.raw;
import static org.ops4j.sisyphos.http.api.Dsl.status;

import java.time.Duration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.ops4j.sisyphos.http.api.HttpConfiguration;
import org.ops4j.sisyphos.http.api.HttpRequestBuilder;

@ApplicationScoped
public class Simulations {

    public void defineSimulations(@Observes @Initialized(ApplicationScoped.class) Object event) {
        HttpConfiguration httpConfig = httpConfig()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .acceptHeader(MediaType.APPLICATION_JSON)
            .contentTypeHeader(MediaType.APPLICATION_JSON);

        HttpRequestBuilder listPosts = http("List all posts")
            .get("posts")
            .check(status().is(Status.OK.getStatusCode()));

        HttpRequestBuilder createPost = http("Create post")
            .post("posts")
            .body(raw("{\"title\":\"New post\", \"body\": \"bla\", \"userId\": 17}"))
            .check(status().is(Status.CREATED.getStatusCode()));

        simulation("jsonplaceholder")
            .withConfig(httpConfig)
            .withReportDir("jsonplaceholder-itest")
            .withScenario(scenario("jsontest")
                .withUsers(atInterval(10, Duration.ofMillis(100)))
                .repeat(1,
                    exec(listPosts)
                    .pause(1, 3)
                    .exec(createPost)
                    .pause(1, 3)));
        }
    }
