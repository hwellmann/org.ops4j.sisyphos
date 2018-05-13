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
package org.ops4j.sisyphos.core.builder;

import org.ops4j.sisyphos.api.action.ActionBuilder;
import org.ops4j.sisyphos.api.session.Action;
import org.ops4j.sisyphos.api.session.Session;

import io.vavr.collection.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * @author Harald Wellmann
 *
 */
public interface FluxBuilder {

    default List<ActionBuilder> actionBuilders() {
        return List.empty();
    }

    default Flux<Session> buildFlux(ScenarioContext context) {
        FluxBuilderAdapter adapter = new FluxBuilderAdapter();
        return Flux.concat(actionBuilders().map(a -> adapter.adapt(a)).map(a -> a.buildFlux(context)).toList());
    }

    default Flux<Session> applyToContextSession(Flux<Action> action) {
        return action.flatMap(a -> Mono.subscriberContext().map(ctx -> applyAction(a, ctx)));
    }

    default Session applyAction(Action action, Context context) {
        Session session = context.get("_session");
        return action.apply(session);
    }
}
