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

import org.ops4j.sisyphos.api.action.RepeatBuilder;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.core.common.ScenarioContext;

import reactor.core.publisher.Flux;
import reactor.retry.Repeat;

public class RepeatFluxBuilder implements FluxBuilder {


    private RepeatBuilder repeatBuilder;

    public RepeatFluxBuilder(RepeatBuilder repeatBuilder) {
        this.repeatBuilder = repeatBuilder;
    }

    @Override
    public Flux<Session> buildFlux(ScenarioContext context) {
        FluxBuilderAdapter adapter = new FluxBuilderAdapter();
        FluxBuilder fluxBuilder = adapter.adapt(repeatBuilder.getStepBuilder());
        return Repeat.times(repeatBuilder.getNumRepetitions()).apply(fluxBuilder.buildFlux(context));
    }
}
