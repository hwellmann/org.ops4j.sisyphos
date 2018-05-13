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
package org.ops4j.sisyphos.core.runner;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;
import org.ops4j.sisyphos.api.feed.CsvFeedBuilder;
import org.ops4j.sisyphos.api.feed.FeedStrategy;
import org.ops4j.sisyphos.api.session.Action;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.core.common.ScenarioContext;
import org.ops4j.sisyphos.core.feed.FeedSequenceBuilderAdapter;
import org.ops4j.sisyphos.core.session.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vavr.collection.Iterator;
import io.vavr.collection.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Harald Wellmann
 *
 */
public class FluxHttpTest {

    private Logger log = LoggerFactory.getLogger(FluxHttpTest.class);

    private static ScenarioContext context = new DefaultScenarioContext(null, null);

    private static CsvFeedBuilder loginFeedBuilder = new CsvFeedBuilder(
            "login.csv", FeedStrategy.CIRCULAR);

    private static Iterator<Map<String, String>> loginFeed = buildFeed(loginFeedBuilder, context);

    private static Iterator<Map<String, String>> buildFeed(CsvFeedBuilder feed, ScenarioContext context) {
        FeedSequenceBuilderAdapter<String> adapter = new FeedSequenceBuilderAdapter<String>();
        return adapter.adapt(feed).buildFeed(context);
    }

    private Session consumeLogin(Session session) {
        if (!loginFeed.hasNext()) {
            session.markAsFailed();
            throw new IllegalStateException("Feed has no more elements");
        }
        session.setAttributes(loginFeed.next().toJavaMap());
        return session;
    }

    @Test
    public void shouldFeed() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Supplier<String> supplier = () -> loginFeed.next().get("login").get();
        Flux<String> scenario = Flux.just(supplier).repeat(20)
                .map(Supplier::get).doOnTerminate(latch::countDown);
        scenario.subscribe(log::info);
        latch.await();
    }

    @Test
    public void shouldDelay() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Flux<String> scenario = Flux.just("one", "two")
                .concatWith(Flux.<String> empty().delaySubscription(Duration.ofSeconds(1)))
                .concatWith(Flux.just("four"))
                .doOnTerminate(latch::countDown);
        scenario.subscribe(log::info);
        latch.await();
    }

    @Test
    public void shouldDelay2() throws InterruptedException {
        Function<Integer, Flux<String>> f0 = i -> Flux.just("one", "two");
        Function<Integer, Flux<String>> f1 = i -> Flux.<String> empty().delaySubscription(Duration.ofSeconds(1));
        Function<Integer, Flux<String>> f2 = i -> Flux.just("three");
        Function<Integer, Flux<String>> f = i -> Flux.concat(f0.apply(i), f1.apply(i), f2.apply(i));
        CountDownLatch latch = new CountDownLatch(1);

        Flux<String> scenario = f.apply(0)
                .doOnTerminate(latch::countDown);
        scenario.subscribe(log::info);
        latch.await();
    }

    @Test
    public void shouldFeedInParallel() throws InterruptedException {
        int numSessions = 10;
        CountDownLatch latch = new CountDownLatch(numSessions);
        Function<Session, Mono<Session>> scenarioFunction = session -> Flux.<Action>just(this::consumeLogin)
                .delayElements(Duration.ofMillis(200))
                .doOnTerminate(() -> latch.countDown())
                .map(a -> a.execute(session))
                .onErrorReturn(new SessionImpl("<ERROR>"))
                .last().doOnNext(s -> log.info("{}", s.getAttribute("login")));

        Mono<List<Session>> sessions = Flux.range(0, numSessions)
                // .delayElements(Duration.ofMillis(10))
                .map(SessionImpl::new)
                // .parallel()
                .flatMap(scenarioFunction)
                .collectSortedList(Session::compareTo);
        sessions.subscribe();
        latch.await();
    }
}
