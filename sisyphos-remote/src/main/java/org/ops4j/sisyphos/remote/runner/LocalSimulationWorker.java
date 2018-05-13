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

import java.util.logging.Level;

import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.simulation.ScenarioBuilder;
import org.ops4j.sisyphos.api.simulation.SimulationBuilder;
import org.ops4j.sisyphos.api.simulation.SimulationRegistry;
import org.ops4j.sisyphos.core.builder.FluxBuilderAdapter;
import org.ops4j.sisyphos.core.common.ScenarioContext;
import org.ops4j.sisyphos.core.message.SessionEvent;
import org.ops4j.sisyphos.core.message.SessionMessage;
import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.ops4j.sisyphos.core.runner.DefaultScenarioContext;
import org.ops4j.sisyphos.core.session.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class LocalSimulationWorker implements SimulationWorker {

    private static Logger log = LoggerFactory.getLogger(LocalSimulationWorker.class);

    @Override
    public Mono<Long> runUser(FluxSink<StatisticsMessage> messageSink, String simulationName, String scenarioName, long userId) {
        SimulationBuilder simulation = SimulationRegistry.find(simulationName)
            .getOrElseThrow(() -> new IllegalArgumentException("No simulation with name " + simulationName));

        ScenarioBuilder scenario = simulation.getScenarioBuilders().find(sc -> sc.getName().equals(scenarioName))
            .getOrElseThrow(() -> new IllegalArgumentException("No scenario with name " + scenarioName));

        ScenarioContext context = new DefaultScenarioContext(messageSink, simulation.getProtocolConfiguration());

        Session session = new SessionImpl(scenarioName, userId);


        FluxBuilderAdapter adapter = new FluxBuilderAdapter();
        Flux<Session> actions = adapter.adapt(scenario).buildFlux(context);

        return actions
                 .log(LocalSimulationWorker.class.getName(), Level.FINE)
                 .doOnSubscribe(subscription -> onSessionStart(messageSink, session))
                 .doOnError(exc -> log.error("Aborting scenario", exc))
                 .onErrorReturn(new SessionImpl("<ERROR>", userId))
                 .doOnTerminate(() -> onSessionEnd(messageSink, session))
                 .subscriberContext(ctx -> ctx.put("_session", session))
                 .subscribeOn(Schedulers.elastic())
                 .last().map(Session::getUserId);
    }


    private void onSessionStart(FluxSink<StatisticsMessage> messages, Session session) {
        log.info("Starting session {}", session.getUserId());
        messages.next(new SessionMessage(session, SessionEvent.START, System.currentTimeMillis()));
    }

    private void onSessionEnd(FluxSink<StatisticsMessage> messages, Session session) {
        log.info("Terminating session {}", session.getUserId());
        messages.next(new SessionMessage(session, SessionEvent.END, System.currentTimeMillis()));
    }
}
