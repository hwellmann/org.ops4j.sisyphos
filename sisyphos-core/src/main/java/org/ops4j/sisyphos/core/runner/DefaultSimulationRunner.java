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

import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.logging.Level;

import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.simulation.ScenarioBuilder;
import org.ops4j.sisyphos.api.simulation.SimulationBuilder;
import org.ops4j.sisyphos.api.simulation.UserBuilder;
import org.ops4j.sisyphos.core.builder.FluxBuilderAdapter;
import org.ops4j.sisyphos.core.builder.UserFluxBuilder;
import org.ops4j.sisyphos.core.common.ScenarioContext;
import org.ops4j.sisyphos.core.log.LogSubscriber;
import org.ops4j.sisyphos.core.message.RunMessage;
import org.ops4j.sisyphos.core.message.SessionEvent;
import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.ops4j.sisyphos.core.message.UserMessage;
import org.ops4j.sisyphos.core.session.ExtendedSession;
import org.ops4j.sisyphos.core.session.SessionImpl;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vavr.collection.List;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Harald Wellmann
 *
 */
public class DefaultSimulationRunner extends AbstractSimulationRunner {

    private static Logger log = LoggerFactory.getLogger(DefaultSimulationRunner.class);

    private FluxSink<StatisticsMessage> messages;

    @Override
    public void runSimulation(SimulationBuilder simulation) {
        log.info("Running simulation {}", simulation.getName());

        openMessages(simulation.getReportDir());

        ScenarioContext context = new DefaultScenarioContext(messages, simulation.getProtocolConfiguration());

        List<UserBuilder> userBuilders = simulation.getScenarioBuilders().flatMap(s -> s.getUserBuilders());
        int numUsers = userBuilders.map(u -> u.getNumUsers()).sum().intValue();

        messages.next(new RunMessage(simulation.getName(), "", simulation.getId(), System.currentTimeMillis(),
            simulation.getRunDescription()));

        CountDownLatch latch = new CountDownLatch(numUsers);
        simulation.getScenarioBuilders().forEach(s -> runScenario(s, context, latch));
        ConcurrentUtil.waitFor(latch);

        closeMessages();
    }

    private void runScenario(ScenarioBuilder scenarioBuilder, ScenarioContext context,
        CountDownLatch latch) {
        Flux<Session> users = Flux.concat(scenarioBuilder.getUserBuilders()
                .map(UserFluxBuilder::new)
                .map(UserFluxBuilder::build))
            .subscribeOn(Schedulers.elastic());

        FluxBuilderAdapter adapter = new FluxBuilderAdapter();
        Flux<Session> actions = adapter.adapt(scenarioBuilder).buildFlux(context);

        Function<Session, Mono<Session>> sessionFunction = session -> actions
                 .log(DefaultSimulationRunner.class.getName(), Level.FINE)
                 .doOnSubscribe(subscription -> onSessionStart(scenarioBuilder.getName(), session))
                 .doOnError(exc -> log.error("Aborting scenario", exc))
                 .onErrorReturn(new SessionImpl("<ERROR>"))
                 .doOnTerminate(() -> onSessionEnd(session, latch))
                 .subscriberContext(ctx -> ctx.put("_session", session))
                 .last();

        Mono<java.util.List<Session>> simulation = users
            .flatMap(sessionFunction)
            .collectSortedList(Session::compareTo);

        simulation.subscribe(s -> {}, Throwable::printStackTrace);
    }

    private void openMessages(String reportDir) {
        EmitterProcessor<StatisticsMessage> processor = EmitterProcessor.create();
        messages = processor.sink();
        ConnectableFlux<StatisticsMessage> connectableFlux = processor.publish();

        addMessageSubscriber(new LogSubscriber(reportDir));
        getMessageSubscribers().forEach(connectableFlux::subscribe);
        connectableFlux.connect();
    }

    private void closeMessages() {
        messages.complete();
        getMessageSubscribers().forEach(Subscriber::onComplete);
    }


    /**
     * @param session
     * @param latch
     * @return
     */
    private void onSessionEnd(Session session, CountDownLatch latch) {
        messages.next(new UserMessage(session, SessionEvent.END, System.currentTimeMillis()));
        latch.countDown();
    }

    /**
     * @param subscription
     * @param session
     * @return
     */
    private void onSessionStart(String scenarioName, Session session) {
        ((ExtendedSession) session).setScenario(scenarioName);
        messages.next(new UserMessage(session, SessionEvent.START, System.currentTimeMillis()));
    }
}
