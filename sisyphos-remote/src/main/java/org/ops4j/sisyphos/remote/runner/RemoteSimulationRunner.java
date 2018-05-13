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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.simulation.ScenarioBuilder;
import org.ops4j.sisyphos.api.simulation.SimulationBuilder;
import org.ops4j.sisyphos.api.spi.SimulationRunner;
import org.ops4j.sisyphos.api.user.UserBuilder;
import org.ops4j.sisyphos.core.builder.ScenarioContext;
import org.ops4j.sisyphos.core.builder.UserFluxBuilder;
import org.ops4j.sisyphos.core.config.ConfigurationFactory;
import org.ops4j.sisyphos.core.log.LogSubscriber;
import org.ops4j.sisyphos.core.message.SimulationMessage;
import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.ops4j.sisyphos.core.runner.ConcurrentUtil;
import org.ops4j.sisyphos.core.runner.DefaultScenarioContext;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vavr.collection.List;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequestScoped
public class RemoteSimulationRunner implements SimulationRunner {

    private static final int NUM_CONNECTIONS = 100;

    private static Logger log = LoggerFactory.getLogger(RemoteSimulationRunner.class);

    private FluxSink<StatisticsMessage> messages;

    private Subscriber<StatisticsMessage> logSubscriber;

    private Client client;

    private String[] workerUris;

    private AtomicInteger workerIndex = new AtomicInteger();


    @PostConstruct
    public void init() {
        this.client = new ResteasyClientBuilder().connectionPoolSize(NUM_CONNECTIONS).maxPooledPerRoute(NUM_CONNECTIONS).build();
        this.logSubscriber = new LogSubscriber();
        this.workerUris = ConfigurationFactory.configuration().getWorkerUri().split(",");
    }

    @PreDestroy
    public void close() {
        client.close();
    }

    public void setSubscriber(Subscriber<StatisticsMessage> subscriber) {
        this.logSubscriber = subscriber;
    }

    @Override
    public void runSimulation(SimulationBuilder simulation) {
        log.info("running simulation {}", simulation.getName());

        openMessages();

        ScenarioContext context = new DefaultScenarioContext(messages, simulation.getProtocolConfiguration());

        List<UserBuilder> userBuilders = simulation.getScenarioBuilders().flatMap(s -> s.getUserBuilders());
        int numUsers = userBuilders.map(u -> u.getNumUsers()).sum().intValue();

        messages.next(new SimulationMessage(simulation.getName(), simulation.getId(), System.currentTimeMillis(),
            simulation.getRunDescription(), simulation.getReportDir()));

        CountDownLatch latch = new CountDownLatch(numUsers);
        simulation.getScenarioBuilders().forEach(s -> runScenario(s, context, latch, simulation.getName()));
        ConcurrentUtil.waitFor(latch);

        closeMessages();
    }

    private void runScenario(ScenarioBuilder scenarioBuilder, ScenarioContext context,
        CountDownLatch latch, String simulationName) {
        Flux<Session> users = Flux.concat(scenarioBuilder.getUserBuilders()
                .map(UserFluxBuilder::new)
                .map(UserFluxBuilder::build))
            .map(session -> { session.setScenario(scenarioBuilder.getName()); return session; })
            .subscribeOn(Schedulers.elastic())
            .log(RemoteSimulationRunner.class.getName(), Level.FINE);

        Flux<Long> simulation = users
            .flatMap(session -> runSession(session, simulationName, latch));

        simulation.subscribe(s -> {}, Throwable::printStackTrace);
    }

    private Mono<Long> runSession(Session session, String simulationName, CountDownLatch latch) {
        return nextWorker().runUser(messages, simulationName, session.getScenario(), session.getUserId())
            .subscribeOn(Schedulers.elastic())
            .doOnTerminate(() -> latch.countDown() );
    }

    private SimulationWorker nextWorker() {
        int index = workerIndex.getAndIncrement() % workerUris.length;
        WebTarget target = client.target(workerUris[index]);
        return new JaxRsSimulationWorker(target);
    }

    private void openMessages() {
        EmitterProcessor<StatisticsMessage> processor = EmitterProcessor.create();
        messages = processor.sink();
        processor.subscribe(logSubscriber);
    }

    private void closeMessages() {
        messages.complete();
        logSubscriber.onComplete();
    }
}
