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

import io.vavr.collection.List;

/**
 * Entry point for defining simulations.
 * <p>
 * This class only contains static methods, defining a domain specific language for Sisyphos
 * simulations.
 *
 * @author Harald Wellmann
 *
 */
public final class Sisyphos {

    private Sisyphos() {
        // hidden constructor
    }

    /**
     * Creates a simulation with the given name, which must be unique.
     * <p>
     * The new simulation must be configurated using the returned builder. The simulation is
     * automatically registered in the registry.
     *
     * @param name
     *            simulation name
     * @return simulation builder
     */
    public static SimulationBuilder simulation(String name) {
        SimulationBuilder simulation = new SimulationBuilder(name);
        SimulationRegistry.register(simulation);
        return simulation;
    }

    /**
     * Creates a scenario with the given name.
     * <p>
     * The scenario must be configurated using the returned builder.
     *
     * @param name
     *            scenario name.
     * @return scenario builder
     */
    public static ScenarioBuilder scenario(String name) {
        return new ScenarioBuilder(name);
    }

    /**
     * Defines a chain of actions to be executed one after another.
     *
     * @param actionBuilders
     *            actions to be executed
     * @return chain builder
     */
    public static ChainBuilder exec(ActionBuilder... actionBuilders) {
        return new ChainBuilder(List.of(actionBuilders));
    }

    /**
     * Defines a feed based on a CSV file with the given name, using the default queue strategy.
     * <p>
     * The file name is taken relative to the data directory defined in the system configuration.
     *
     * @param fileName
     *            name of CSV file
     * @return feed builder
     */
    public static FeedBuilder<String> csv(String fileName) {
        return new CsvFeedBuilder(fileName, FeedStrategy.QUEUE);
    }

    /**
     * Defines a feed based on a CSV file with the given name and the given feed strategy.
     * <p>
     * The file name is taken relative to the data directory defined in the system configuration.
     * <p>
     * The file must contain column headers which will be used a attribute names to store the feed
     * values in the session.
     *
     * @param fileName
     *            name of CSV file
     * @param strategy
     *            feed strategy
     * @return feed builder
     */
    public static FeedBuilder<String> csv(String fileName, FeedStrategy strategy) {
        return new CsvFeedBuilder(fileName, strategy);
    }

    /**
     * Defines a feed based on the given value list, using the default queue strategy.
     *
     * @param propertyName
     *            attribute key under which the feed value will be stored in the session.
     * @param values
     *            list of values
     * @param <F>
     *            type of feed values
     * @return feed builder
     */
    @SafeVarargs
    public static <F> FeedBuilder<F> feedOf(String propertyName, F... values) {
        return new DirectFeedBuilder<>(propertyName, FeedStrategy.QUEUE, values);
    }

    /**
     * Defines a feed based on the given value list, using the given strategy.
     *
     * @param propertyName
     *            attribute key under which the feed value will be stored in the session.
     * @param strategy
     *            feed strategy
     * @param values
     *            list of values
     * @param <F>
     *            type of feed values
     * @return feed builder
     */
    @SafeVarargs
    public static <F> FeedBuilder<F> feedOf(String propertyName, FeedStrategy strategy,
        F... values) {
        return new DirectFeedBuilder<>(propertyName, strategy, values);
    }

    /**
     * Consumes a map from the given feed and places the map into the current session.
     *
     * @param feedBuilder
     *            feed builder
     * @param <F>
     *            type of feed values
     * @return chain builder
     */
    public static <F> ChainBuilder consume(FeedBuilder<F> feedBuilder) {
        return exec(new ConsumeActionBuilder<F>(feedBuilder));
    }

    /**
     * Defines a request with the given name and the given action.
     * <p>
     * The runtime of this request will be measured and output in the simulation log. Actions not
     * wrapped in a request will never appear in the simulation log.
     *
     * @param name
     *            request name
     * @param action
     *            action executed for the request
     * @return builder
     */
    public static RequestActionBuilder request(String name, Action action) {
        return new RequestActionBuilder(name, action);
    }

    /**
     * Function returning an attribute with the given name from the current session.
     *
     * @param key
     *            attribute name
     * @param <V>
     *            type of attribute value
     * @return function returning attribute value for given name
     */
    public static <V> Function<Session, V> attr(String key) {
        return session -> session.getAttribute(key);
    }

    /**
     * Creates the given number of users which will all start a session immediately.
     * An offset interval can be configured via the returned builder.
     *
     * @param numUsers
     *            number of users
     * @return user builder
     */
    public static UserBuilder users(int numUsers) {
        return new UserBuilder(numUsers);
    }
}
