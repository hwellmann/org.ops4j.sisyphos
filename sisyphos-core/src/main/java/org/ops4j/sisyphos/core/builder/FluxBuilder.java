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
import org.ops4j.sisyphos.core.common.ScenarioContext;

import io.vavr.collection.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * Builds a flux from a list of action builders.
 *
 * @author Harald Wellmann
 *
 */
public interface FluxBuilder {

    /**
     * List of wrapped action builders.
     *
     * @return list of action builders
     */
    default List<ActionBuilder> actionBuilders() {
        return List.empty();
    }

    /**
     * Builds a flux of sessions for the given scenario context.
     *
     * @param context
     *            scenario context
     * @return flux of sessions
     */
    default Flux<Session> buildFlux(ScenarioContext context) {
        FluxBuilderAdapter adapter = new FluxBuilderAdapter();
        return Flux.concat(
            actionBuilders().map(a -> adapter.adapt(a)).map(a -> a.buildFlux(context)).toList());
    }

    /**
     * Applies the given flux of actions to the session stored in the subscriber context of the
     * flux.
     *
     * @param action
     *            flux of actions
     * @return flux of sessions
     */
    default Flux<Session> applyToContextSession(Flux<Action> action) {
        return action.flatMap(a -> Mono.subscriberContext().map(ctx -> applyAction(a, ctx)));
    }

    /**
     * Applies the given action to the session stored in the given context.
     *
     * @param action
     *            action to be applied
     * @param context
     *            subscriber context
     * @return result of applying action to session
     */
    default Session applyAction(Action action, Context context) {
        Session session = context.get("_session");
        return action.apply(session);
    }
}
