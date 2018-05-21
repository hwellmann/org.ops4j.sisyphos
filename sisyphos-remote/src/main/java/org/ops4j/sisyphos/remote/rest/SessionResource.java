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
package org.ops4j.sisyphos.remote.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.ops4j.sisyphos.core.log.OutputStreamSubscriber;
import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.ops4j.sisyphos.core.runner.ConcurrentUtil;
import org.ops4j.sisyphos.remote.runner.LocalSimulationWorker;
import org.ops4j.sisyphos.remote.runner.SimulationWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@RequestScoped
@Path("session")
public class SessionResource {

    private static Logger log = LoggerFactory.getLogger(SessionResource.class);

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{simulation}/{scenario}/{userId}")
    public StreamingOutput runSimulation(@PathParam("simulation") String simulationName, @PathParam("scenario") String scenarioName, @PathParam("userId") long userId) {
        return os -> runSimulation(os, simulationName, scenarioName, userId);
    }

    private void runSimulation(OutputStream os, String simulationName, String scenarioName, long userId) {
        SimulationWorker worker = new LocalSimulationWorker();

        EmitterProcessor<StatisticsMessage> processor = EmitterProcessor.create();
        FluxSink<StatisticsMessage> messageSink = processor.sink();

        Mono<Long> mono = worker.runUser(messageSink, simulationName, scenarioName, userId);

        OutputStreamSubscriber oss = new OutputStreamSubscriber(os);
        processor.subscribe(oss);

        CountDownLatch latch = new CountDownLatch(1);
        mono.subscribe(null, exc -> log.error("error", exc), () -> latch.countDown());

        try {
            ConcurrentUtil.waitFor(latch);
            messageSink.complete();
            oss.onComplete();
            os.close();
        }
        catch (IOException exc) {
            log.error("", exc);
        }
    }
}
