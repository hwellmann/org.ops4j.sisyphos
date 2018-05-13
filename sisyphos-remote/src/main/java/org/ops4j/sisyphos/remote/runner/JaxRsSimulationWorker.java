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
package org.ops4j.sisyphos.remote.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ops4j.sisyphos.core.log.StringToMessageAdapter;
import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Dependent
public class JaxRsSimulationWorker implements SimulationWorker {

    private static Logger log = LoggerFactory.getLogger(JaxRsSimulationWorker.class);

    private WebTarget target;

    public JaxRsSimulationWorker(WebTarget target) {
        this.target = target;
    }

    @Override
    public Mono<Long> runUser(FluxSink<StatisticsMessage> messageSink, String simulationName, String scenarioName, long userId) {
        return Mono.defer(() -> runUserViaClient(messageSink, simulationName, scenarioName, userId));
    }

    private Mono<Long> runUserViaClient(FluxSink<StatisticsMessage> messageSink, String simulationName,
        String scenarioName, long userId) {
        Response response = target.path("session/{simulation}/{scenario}/{userId}")
            .resolveTemplate("simulation", simulationName)
            .resolveTemplate("scenario", scenarioName)
            .resolveTemplate("userId", userId)
            .request(MediaType.TEXT_PLAIN)
            .post(Entity.text(""));

        InputStream is = response.readEntity(InputStream.class);

        try {
            processResponse(messageSink, is);
        }
        catch (IOException exc) {
            log.error("Error processing response", exc);
        }
        response.close();
        return Mono.just(userId);
    }

    private void processResponse(FluxSink<StatisticsMessage> messageSink, InputStream is) throws IOException {
        StringToMessageAdapter adapter = new StringToMessageAdapter();
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String line = null;
        while ((line = reader.readLine()) != null) {
            messageSink.next(adapter.adapt(line));
        }
    }
}
