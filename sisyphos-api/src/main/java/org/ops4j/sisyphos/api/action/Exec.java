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
import org.ops4j.sisyphos.api.simulation.ScenarioBuilder;

import io.vavr.collection.Iterator;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;

/**
 * @author Harald Wellmann
 *
 */
public interface Exec<T> extends ActionBuilder {

    List<ActionBuilder> actionBuilders();

    T newInstance(List<ActionBuilder> actionBuilders);

    default T exec(ActionBuilder actionBuilder) {
        return chain(Iterator.of(actionBuilder));
    }

    default T exec(ScenarioBuilder scenarioBuilder) {
        return chain(scenarioBuilder.actionBuilders().iterator());
    }

    default T chain(Iterator<ActionBuilder> newActionBuilders) {
        return newInstance(Iterator.concat(actionBuilders(), newActionBuilders).toList());
    }

    default T exec(ChainBuilder... chains) {
        return exec(List.of(chains));
    }

    default T exec(List<ChainBuilder> chains) {
        return chain(chains.iterator().flatMap(c -> c.actionBuilders()));
    }


    default T repeat(int times, ActionBuilder step) {
        return exec(new RepeatBuilder(times, step));
    }

    default T retry(int times, ActionBuilder step) {
        return exec(new RetryBuilder(times, step));
    }

    default T during(Duration duration, ActionBuilder step) {
        return exec(new DuringBuilder(duration, step));
    }

    default T pause(Duration duration) {
        return exec(new PauseBuilder(duration, duration));
    }

    default T pause(Duration minDuration, Duration maxDuration) {
        return exec(new PauseBuilder(minDuration, maxDuration));
    }

    default T pause(long minSeconds, long maxSeconds) {
        return exec(new PauseBuilder(Duration.ofSeconds(minSeconds), Duration.ofSeconds(maxSeconds)));
    }

    default T pause(long seconds) {
        return pause(Duration.ofSeconds(seconds));
    }

    default T uniformRandomSwitch(ActionBuilder... cases) {
        return exec(new RandomSwitchBuilder(List.of(cases)));
    }

    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2) {
        return exec(new RandomSwitchBuilder(LinkedHashMap.of(w1, a1, w2, a2)));
    }

    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2, double w3, ActionBuilder a3) {
        return exec(new RandomSwitchBuilder(LinkedHashMap.of(w1, a1, w2, a2, w3, a3)));
    }

    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2, double w3, ActionBuilder a3, double w4, ActionBuilder a4) {
        return exec(new RandomSwitchBuilder(LinkedHashMap.of(w1, a1, w2, a2, w3, a3, w4, a4)));
    }

    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2, double w3, ActionBuilder a3, double w4, ActionBuilder a4, double w5, ActionBuilder a5) {
        return exec(new RandomSwitchBuilder(LinkedHashMap.of(w1, a1, w2, a2, w3, a3, w4, a4, w5, a5)));
    }

    default T randomSwitch(double w1, ActionBuilder a1, double w2, ActionBuilder a2, double w3, ActionBuilder a3, double w4, ActionBuilder a4, double w5, ActionBuilder a5, double w6, ActionBuilder a6) {
        return exec(new RandomSwitchBuilder(LinkedHashMap.of(w1, a1, w2, a2, w3, a3, w4, a4, w5, a5, w6, a6)));
    }

    default T doIf(Predicate<Session> condition, ActionBuilder ifAction) {
        return exec(new IfBuilder(condition, ifAction, new EmptyActionBuilder()));
    }

    default T doIfElse(Predicate<Session> condition, ActionBuilder ifAction, ActionBuilder elseAction) {
        return exec(new IfBuilder(condition, ifAction, elseAction));
    }

    default public <F> T consume(FeedBuilder<F> feedBuilder) {
        return exec(new ConsumeActionBuilder<F>(feedBuilder));
    }

    default T group(String groupName, ChainBuilder chain) {
        return chain(chain.actionBuilders().prepend(new GroupStartActionBuilder(groupName)).append(new GroupEndActionBuilder(groupName)).iterator());
    }
}
