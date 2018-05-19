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

import org.ops4j.sisyphos.api.action.RequestActionBuilder;
import org.ops4j.sisyphos.api.session.Action;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.core.common.ScenarioContext;
import org.ops4j.sisyphos.core.message.RequestMessage;
import org.ops4j.sisyphos.core.session.ExtendedSession;

import reactor.core.publisher.Flux;

/**
 * @author Harald Wellmann
 *
 */
public class RequestActionFluxBuilder implements FluxBuilder {

    private RequestActionBuilder requestActionBuilder;

    public RequestActionFluxBuilder(RequestActionBuilder requestActionBuilder) {
        this.requestActionBuilder = requestActionBuilder;
    }

    @Override
    public Flux<Session> buildFlux(ScenarioContext context) {
        return applyToContextSession(Flux.just(session -> executeAndLog(context, session)));
    }

    private Session executeAndLog(ScenarioContext context, Session session) {
        Action action = requestActionBuilder.getAction();
        String name = requestActionBuilder.getName();

        long requestTimestamp = System.currentTimeMillis();
        action.execute(session);

        ExtendedSession extSession = (ExtendedSession) session;
        long responseTimestamp = System.currentTimeMillis();
        long responseTime = responseTimestamp - requestTimestamp;
        extSession.accumulateResponseTime(responseTime);

        RequestMessage msg = new RequestMessage(session.getScenario(), session.getUserId(),
            extSession.getGroupHierarchy(), name, requestTimestamp, responseTimestamp, session.getStatus());
        String message = session.getAttribute("_message");
        if (message != null) {
            msg.setMessage(message);
        }
        context.messageSink().next(msg);
        return session;
    }
}
