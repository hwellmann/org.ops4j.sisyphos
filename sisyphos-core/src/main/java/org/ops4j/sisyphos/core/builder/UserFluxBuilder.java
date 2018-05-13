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

import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.user.AtIntervalUserBuilder;
import org.ops4j.sisyphos.api.user.AtOnceUserBuilder;
import org.ops4j.sisyphos.api.user.UserBuilder;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class UserFluxBuilder {


    private UserBuilder userBuilder;

    public UserFluxBuilder(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
    }

    public Flux<Session> build() {
        if (userBuilder instanceof AtOnceUserBuilder) {
            return buildAtOnce();
        }
        if (userBuilder instanceof AtIntervalUserBuilder) {
            return buildAtInterval((AtIntervalUserBuilder) userBuilder);
        }

        throw new IllegalArgumentException(userBuilder.getClass().getName());
    }

    private Flux<Session> buildAtInterval(AtIntervalUserBuilder atInterval) {
        SessionFactory factory = SessionFactory.create();
        return Flux.range(0, userBuilder.getNumUsers())
            .delayElements(atInterval.getInterval(), Schedulers.parallel())
            .map(i -> factory.newSession(userBuilder.nextUserId()));
    }

    private Flux<Session> buildAtOnce() {
        SessionFactory factory = SessionFactory.create();
        return Flux.range(0, userBuilder.getNumUsers()).map(i -> factory.newSession(userBuilder.nextUserId()));
    }
}
