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
package org.ops4j.sisyphos.api.action;

import java.time.Duration;
import java.util.function.Predicate;

import org.ops4j.sisyphos.api.feed.FeedBuilder;
import org.ops4j.sisyphos.api.session.Session;

import io.vavr.collection.Iterator;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;

/**
 * Interface with common methods shared by various builders.
 *
 * @author Harald Wellmann
 *
 */
public interface Exec<T> extends ActionBuilder {

    /**
     * Gets the list of wrapped actions builders.
     * <p>
     * <strong>Not part of public API.</strong>
     *
     * @return list of action builders
     */
    List<ActionBuilder> actionBuilders();

    /**
     * Creates a new instance of the concrete class implementing this interface.
     * <p>
     * <strong>Not part of public API.</strong>
     *
     * @param actionBuilders
     *            list of wrapped action builders
     * @return new builder
     */
    T newInstance(List<ActionBuilder> actionBuilders);

    /**
     * Executes the given action.
     *
     * @param actionBuilder
     *            action
     * @return builder
     */
    default T exec(ActionBuilder actionBuilder) {
        return chain(Iterator.of(actionBuilder));
    }

    /**
     * Creates a composite of the given actions.
     * <p>
     * <strong>Not part of public API.</strong>
     *
     * @param newActionBuilders
     *            element action builders
     * @return builder
     */
    default T chain(Iterator<ActionBuilder> newActionBuilders) {
        return newInstance(Iterator.concat(actionBuilders(), newActionBuilders).toList());
    }

    /**
     * Executes the given actions in the given order.
     *
     * @param chains
     *            composite actions
     * @return builder
     */
    default T exec(ChainBuilder... chains) {
        return exec(List.of(chains));
    }

    /**
     * Executes the given actions in the given order.
     *
     * @param chains
     *            composite actions
     * @return builder
     */
    default T exec(List<ChainBuilder> chains) {
        return chain(chains.iterator().flatMap(c -> c.actionBuilders()));
    }

    /**
     * Executes and repeats the given action the given number of times.
     * <p>
     * Example: For times = 2, the action will be executed 3 times in total.
     *
     * @param times
     *            number of times the action will be repeated.
     * @param step
     *            action to be repeated
     * @return builder
     */
    default T repeat(int times, ActionBuilder step) {
        return exec(new RepeatBuilder(times, step));
    }

    /**
     * Executes the given action and retries on failure at most a given number of times. If the
     * action does not succeed in the last try, the current session is marked as failed.
     * <p>
     * Example: For times = 2, the action may be executed up to 3 times in total.
     *
     * @param times
     *            number of times the action will be repeated.
     * @param step
     *            action to be repeated
     * @return builder
     */
    default T retry(int times, ActionBuilder step) {
        return exec(new RetryBuilder(times, step));
    }

    /**
     * Repeats the given action for the given duration.
     *
     * @param duration
     *            duration
     * @param step
     *            action to be repeated
     * @return builder
     */
    default T during(Duration duration, ActionBuilder step) {
        return exec(new DuringBuilder(duration, step));
    }

    /**
     * Pauses for the given duration.
     *
     * @param duration
     *            duration
     * @return builder
     */
    default T pause(Duration duration) {
        return exec(new PauseBuilder(duration, duration));
    }

    /**
     * Pauses for a random duration contained in the given interval.
     *
     * @param minDuration
     *            minimum duration
     * @param maxDuration
     *            maximum duration
     * @return builder
     */
    default T pause(Duration minDuration, Duration maxDuration) {
        return exec(new PauseBuilder(minDuration, maxDuration));
    }

    /**
     * Pauses for a random duration contained in the given interval.
     *
     * @param minSeconds
     *            minimum duration in seconds
     * @param maxSeconds
     *            maximum duration in seconds
     * @return builder
     */
    default T pause(long minSeconds, long maxSeconds) {
        return exec(
            new PauseBuilder(Duration.ofSeconds(minSeconds), Duration.ofSeconds(maxSeconds)));
    }

    /**
     * Pauses for the given duration.
     *
     * @param seconds
     *            duration in seconds
     * @return builder
     */
    default T pause(long seconds) {
        return pause(Duration.ofSeconds(seconds));
    }

    /**
     * Randomly selects and executes one of the given actions with uniform probability.
     *
     * @param cases
     *            actions to be selected
     * @return builder
     */
    default T uniformRandomSwitch(ActionBuilder... cases) {
        return exec(new RandomSwitchBuilder(List.of(cases)));
    }

    /**
     * Randomly selects and executes one of the given actions with weighted probabilities.
     * <p>
     * The weights can be arbitrary positive numbers. The probability per action will be the given
     * weight divided by the sum of all weights.
     *
     * @param w1
     *            weight of first action
     * @param a1
     *            first action
     * @param w2
     *            weight of second action
     * @param a2
     *            second action
     * @return builder
     */
    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2) {
        return exec(new RandomSwitchBuilder(LinkedHashMap.of(w1, a1, w2, a2)));
    }

    /**
     * Randomly selects and executes one of the given actions with weighted probabilities.
     * <p>
     * The weights can be arbitrary positive numbers. The probability per action will be the given
     * weight divided by the sum of all weights.
     *
     * @param w1
     *            weight of first action
     * @param a1
     *            first action
     * @param w2
     *            weight of second action
     * @param a2
     *            second action
     * @param w3
     *            weight of third action
     * @param a3
     *            third action
     * @return builder
     */
    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2, double w3,
        ActionBuilder a3) {
        return exec(new RandomSwitchBuilder(LinkedHashMap.of(w1, a1, w2, a2, w3, a3)));
    }

    /**
     * Randomly selects and executes one of the given actions with weighted probabilities.
     * <p>
     * The weights can be arbitrary positive numbers. The probability per action will be the given
     * weight divided by the sum of all weights.
     *
     * @param w1
     *            weight of first action
     * @param a1
     *            first action
     * @param w2
     *            weight of second action
     * @param a2
     *            second action
     * @param w3
     *            weight of third action
     * @param a3
     *            third action
     * @param w4
     *            weight of fourth action
     * @param a4
     *            fourth action
     * @return builder
     */
    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2, double w3,
        ActionBuilder a3, double w4, ActionBuilder a4) {
        return exec(new RandomSwitchBuilder(LinkedHashMap.of(w1, a1, w2, a2, w3, a3, w4, a4)));
    }

    /**
     * Randomly selects and executes one of the given actions with weighted probabilities.
     * <p>
     * The weights can be arbitrary positive numbers. The probability per action will be the given
     * weight divided by the sum of all weights.
     *
     * @param w1
     *            weight of first action
     * @param a1
     *            first action
     * @param w2
     *            weight of second action
     * @param a2
     *            second action
     * @param w3
     *            weight of third action
     * @param a3
     *            third action
     * @param w4
     *            weight of fourth action
     * @param a4
     *            fourth action
     * @param w5
     *            weight of fifth action
     * @param a5
     *            fifth action
     * @return builder
     */
    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2, double w3,
        ActionBuilder a3, double w4, ActionBuilder a4, double w5, ActionBuilder a5) {
        return exec(
            new RandomSwitchBuilder(LinkedHashMap.of(w1, a1, w2, a2, w3, a3, w4, a4, w5, a5)));
    }

    /**
     * Randomly selects and executes one of the given actions with weighted probabilities.
     * <p>
     * The weights can be arbitrary positive numbers. The probability per action will be the given
     * weight divided by the sum of all weights.
     *
     * @param w1
     *            weight of first action
     * @param a1
     *            first action
     * @param w2
     *            weight of second action
     * @param a2
     *            second action
     * @param w3
     *            weight of third action
     * @param a3
     *            third action
     * @param w4
     *            weight of fourth action
     * @param a4
     *            fourth action
     * @param w5
     *            weight of fifth action
     * @param a5
     *            fifth action
     * @param w6
     *            weight of sixth action
     * @param a6
     *            sixth action
     * @return builder
     */
    // CHECKSTYLE:SKIP
    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2, double w3,
        ActionBuilder a3, double w4, ActionBuilder a4, double w5, ActionBuilder a5, double w6,
        ActionBuilder a6) {
        return exec(new RandomSwitchBuilder(
            LinkedHashMap.of(w1, a1, w2, a2, w3, a3, w4, a4, w5, a5, w6, a6)));
    }

    /**
     * Conditionally executes the given action, if the given predicate is true for the current session.
     * @param condition session predicate
     * @param ifAction action to be executed
     * @return builder
     */
    default T doIf(Predicate<Session> condition, ActionBuilder ifAction) {
        return exec(new IfBuilder(condition, ifAction, new EmptyActionBuilder()));
    }

    /**
     * Conditionally executes one of two actions, depending on the given predicate.
     * @param condition session predicate
     * @param ifAction action to be executed if condtion is true
     * @param elseAction action to be executed if condtion is false
     * @return builder
     */
    default T doIfElse(Predicate<Session> condition, ActionBuilder ifAction,
        ActionBuilder elseAction) {
        return exec(new IfBuilder(condition, ifAction, elseAction));
    }

    /**
     * Consumes a map of attributes from the given feed and places them in the current session.
     * @param feedBuilder feed builder
     * @param <F> type of feed value
     * @return builder
     */
    default <F> T consume(FeedBuilder<F> feedBuilder) {
        return exec(new ConsumeActionBuilder<F>(feedBuilder));
    }

    /**
     * Defines a named group of actions.
     * <p>
     * Groups can be used to structure the generated reports by collapsing or expanding the details of the group.
     * <p>
     * A group also aggregates the execution time of the contained actions.
     * <p>
     * Groups may be nested.
     *
     * @param groupName group name
     * @param chain chain of actions
     * @return builder
     */
    default T group(String groupName, ChainBuilder chain) {
        return chain(chain.actionBuilders().prepend(new GroupStartActionBuilder(groupName))
            .append(new GroupEndActionBuilder(groupName)).iterator());
    }
}
