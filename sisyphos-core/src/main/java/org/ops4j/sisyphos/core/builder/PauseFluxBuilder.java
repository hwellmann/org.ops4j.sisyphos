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
package org.ops4j.sisyphos.core.builder;

import java.time.Duration;
import java.util.Random;

import org.ops4j.sisyphos.api.action.PauseBuilder;
import org.ops4j.sisyphos.api.session.Session;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class PauseFluxBuilder implements FluxBuilder {


    private PauseBuilder pauseBuilder;
    private Random random;

    public PauseFluxBuilder(PauseBuilder pauseBuilder) {
        this.pauseBuilder = pauseBuilder;
        this.random = new Random();
    }

    @Override
    public Flux<Session> buildFlux(ScenarioContext context) {
        return Mono.delay(duration(), Schedulers.parallel()).thenMany(Flux.empty());
    }

    private Duration duration() {
        Duration minDuration = pauseBuilder.getMinDuration();
        Duration maxDuration = pauseBuilder.getMaxDuration();

        if (minDuration.equals(maxDuration)) {
            return minDuration;
        }

        long diff = maxDuration.minus(minDuration).toMillis();
        return minDuration.plusMillis(random.nextLong() % diff);
    }

}
