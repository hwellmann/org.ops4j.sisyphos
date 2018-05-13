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
package org.ops4j.sisyphos.api.simulation;

import java.time.Duration;
import java.util.function.Function;

import org.ops4j.sisyphos.api.action.ActionBuilder;
import org.ops4j.sisyphos.api.action.ChainBuilder;
import org.ops4j.sisyphos.api.action.ConsumeActionBuilder;
import org.ops4j.sisyphos.api.action.RequestActionBuilder;
import org.ops4j.sisyphos.api.feed.CsvFeedBuilder;
import org.ops4j.sisyphos.api.feed.DirectFeedBuilder;
import org.ops4j.sisyphos.api.feed.FeedBuilder;
import org.ops4j.sisyphos.api.feed.FeedStrategy;
import org.ops4j.sisyphos.api.session.Action;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.user.AtIntervalUserBuilder;
import org.ops4j.sisyphos.api.user.AtOnceUserBuilder;
import org.ops4j.sisyphos.api.user.UserBuilder;

import io.vavr.collection.List;

/**
 * @author Harald Wellmann
 *
 */
public class Sisyphos {


    private Sisyphos() {
        // hidden constructor
    }

    public static SimulationBuilder simulation(String name) {
        SimulationBuilder simulation = new SimulationBuilder(name);
        SimulationRegistry.register(name, simulation);
        return simulation;
    }

    public static ScenarioBuilder scenario(String name) {
        return new ScenarioBuilder(name);
    }

    public static ChainBuilder exec(ActionBuilder... actionBuilders) {
        return new ChainBuilder(List.of(actionBuilders));
    }

    public static FeedBuilder<String> csv(String fileName) {
        return new CsvFeedBuilder(fileName, FeedStrategy.QUEUE);
    }

    public static FeedBuilder<String> csv(String fileName, FeedStrategy strategy) {
        return new CsvFeedBuilder(fileName, strategy);
    }

    @SafeVarargs
    public static <F> FeedBuilder<F> feedOf(String propertyName, F... values) {
        return new DirectFeedBuilder<>(propertyName, FeedStrategy.QUEUE, values);
    }

    @SafeVarargs
    public static <F> FeedBuilder<F> feedOf(String propertyName, FeedStrategy strategy, F... values) {
        return new DirectFeedBuilder<>(propertyName, strategy, values);
    }

    public static <F> ChainBuilder consume(FeedBuilder<F> feedBuilder) {
        return exec(new ConsumeActionBuilder<F>(feedBuilder));
    }

    public static RequestActionBuilder request(String name, Action action) {
        return new RequestActionBuilder(name, action);
    }

    public static Function<Session, String> attr(String key) {
        return session -> session.getAttribute(key, String.class);
    }


    public static UserBuilder atOnce(int numUsers) {
        return new AtOnceUserBuilder(numUsers);
    }

    public static UserBuilder atInterval(int numUsers, Duration interval) {
        return new AtIntervalUserBuilder(numUsers, interval);
    }


}
