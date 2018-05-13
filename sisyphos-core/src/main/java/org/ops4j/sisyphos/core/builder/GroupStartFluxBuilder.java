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

import org.ops4j.sisyphos.api.action.GroupStartActionBuilder;
import org.ops4j.sisyphos.api.session.Action;
import org.ops4j.sisyphos.api.session.Session;

import reactor.core.publisher.Flux;

public class GroupStartFluxBuilder implements FluxBuilder {


    private GroupStartActionBuilder groupStart;

    public GroupStartFluxBuilder(GroupStartActionBuilder groupStart) {
        this.groupStart = groupStart;
    }

    @Override
    public Flux<Session> buildFlux(ScenarioContext context) {
        return applyToContextSession(Flux.<Action>just(this::startGroup));
    }

    private Session startGroup(Session session) {
        session.startGroup(groupStart.getGroupName());
        return session;
    }
}
