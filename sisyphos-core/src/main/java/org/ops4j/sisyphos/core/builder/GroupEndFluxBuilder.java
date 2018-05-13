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

import java.util.List;

import org.ops4j.sisyphos.api.action.Group;
import org.ops4j.sisyphos.api.action.GroupEndActionBuilder;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.core.message.GroupMessage;

import reactor.core.publisher.Flux;

public class GroupEndFluxBuilder implements FluxBuilder {


    private GroupEndActionBuilder groupEnd;

    public GroupEndFluxBuilder(GroupEndActionBuilder groupEnd) {
        this.groupEnd = groupEnd;
    }

    @Override
    public Flux<Session> buildFlux(ScenarioContext context) {
        return applyToContextSession(Flux.just(session -> endGroup(session, context)));
    }

    private Session endGroup(Session session, ScenarioContext context) {
        List<String> groups = session.getGroupHierarchy();
        Group group = session.endGroup(groupEnd.getGroupName());
        GroupMessage groupMessage = new GroupMessage(session.getScenario(), session.getUserId(),
            groups, group.getStartDate(), group.getEndDate(),
            group.getCumulatedResponseTime(), session.getStatus());
        context.messageSink().next(groupMessage);
        return session;
    }
}
