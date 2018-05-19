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
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.ops4j.sisyphos.api.session.Action;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.core.session.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vavr.collection.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.retry.Repeat;
import reactor.retry.Retry;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

/**
 * @author Harald Wellmann
 *
 */
public class OperatorTest {

    private static Logger log = LoggerFactory.getLogger(OperatorTest.class);
    private int numTries;

    @Test
    public void repeatWhenDuring() throws InterruptedException {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4).delayElements(Duration.ofMillis(25))
            .repeatWhen(Repeat.times(Integer.MAX_VALUE).timeout(Duration.ofMillis(301)));

        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
            .verifyComplete();
    }

    @Test
    public void repeatDuring() throws InterruptedException {
        Flux<Integer> flux = Repeat.times(Integer.MAX_VALUE).timeout(Duration.ofMillis(301))
            .apply(Flux.just(1, 2, 3, 4).delayElements(Duration.ofMillis(25)));

        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
            .verifyComplete();
    }

    @Test
    public void repeatTimes() throws InterruptedException {
        Flux<Integer> flux = Repeat.times(2).apply(Flux.just(1, 2, 3, 4));

        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
            .verifyComplete();
    }

    private Flux<Integer> failFirst(AtomicInteger numFailures) {
        if (numFailures.getAndIncrement() == 0) {
            return Flux.error(new IllegalStateException());
        }
        else {
            return Flux.just(3);
        }
    }

    @Test
    public void shouldFailWithoutRetry() {
        AtomicInteger numFailures = new AtomicInteger();
        Flux<Integer> flux = Flux.just(1, 2).concatWith(Flux.defer(() -> failFirst(numFailures)));

        StepVerifier.create(flux)
            .expectNext(1, 2)
            .verifyError(IllegalStateException.class);

    }

    @Test
    public void shouldNotFailWithRetry() {
        AtomicInteger numFailures = new AtomicInteger();
        Flux<Integer> flux = Flux.just(1, 2).concatWith(Flux.defer(() -> failFirst(numFailures)));
        Flux<Integer> retryFlux = Retry.any().retryMax(1).apply(flux);

        StepVerifier.create(retryFlux)
            .expectNext(1, 2, 1, 2, 3)
            .verifyComplete();

    }

    @Test
    public void shouldSwitchOnFunction() {
        Integer[][] numbers = {
            { 3, 6, 9 },
            { 4, 8, 12, 16 },
            { 5, 10, 15, 20, 25 }
        };
        Flux<Integer> three = Flux.just(numbers[0]);
        Flux<Integer> four = Flux.just(numbers[1]);
        Flux<Integer> five = Flux.just(numbers[2]);
        List<Flux<Integer>> cases = List.of(three, four, five);

        for (int select = 0; select < numbers.length; select++) {
            Flux<Integer> flux = Flux.just(select).switchMap(i -> cases.get(i));

            StepVerifier.create(flux)
                .expectNext(numbers[select])
                .verifyComplete();
        }
    }

    @Test
    public void shouldDoIf() {
        Flux<Integer> even = Flux.just(2, 4, 6);
        Flux<Integer> odd = Flux.just(11, 13, 15);

        Flux<Integer> flux = even.filterWhen(i -> Mono.just(i < 10)).switchIfEmpty(odd);

        StepVerifier.create(flux)
            .expectNext(2, 4, 6)
            .verifyComplete();
    }

    @Test
    public void shouldDoElse() {
        Flux<Integer> even = Flux.just(2, 4, 6);
        Flux<Integer> odd = Flux.just(11, 13, 15);

        Flux<Integer> flux = even.filterWhen(i -> Mono.just(i > 10)).switchIfEmpty(odd);

        StepVerifier.create(flux)
            .expectNext(11, 13, 15)
            .verifyComplete();
    }

    @Test
    public void shouldConcat() {
        Flux<Integer> even = Flux.just(2, 4, 6);
        Flux<Integer> odd = Flux.just(11, 13, 15);

        Flux<Integer> flux = even
            .concatWith(odd)
            .flatMap(i ->
                Mono.subscriberContext().map(ctx -> {
                    log.debug("{}", ctx);
                    return i;
                }))
            .subscriberContext(ctx -> ctx.put("foo", "bar"));

        StepVerifier.create(flux)
            .expectNext(2, 4, 6, 11, 13, 15)
            .verifyComplete();
    }

    private Session storeOne(Session session) {
        session.setAttribute("one", 1);
        return session;
    }

    private Session storeTwo(Session session) {
        session.setAttribute("two", 2);
        return session;
    }

    private Session doStep(Session session, Step step) {
        Session session2 = step.getAction().apply(session);
        step.setSession(session2);
        log.debug("{}", session2);
        return session2;
    }

    private Session doStep(Session session, Action action) {
        Session session2 = action.apply(session);
        log.debug("{}", session2);
        return session2;
    }

    @Test
    public void shouldStoreOne() throws InterruptedException {
        Flux<Action> one = Flux.just(this::storeOne);
        Flux<Action> two = Flux.just(this::storeTwo);
        Session session = new SessionImpl(1);
        session.setAttribute("zero", 0);

        Flux<Action> fluxExt = one
            .filterWhen(t -> Mono.subscriberContext().map(ctx ->
                    ctx.<Session>get("session").getAttribute("zero") != null))
            .switchIfEmpty(two)
            .subscriberContext(ctx -> ctx.put("session", session));

        StepVerifier.create(fluxExt.reduce(session, this::doStep))
            .expectNextMatches(s -> s.getAttribute("one").equals(1))
            .verifyComplete();
    }

    @Test
    public void shouldStoreTwo() throws InterruptedException {
        Flux<Action> one = Flux.just(this::storeOne);
        Flux<Action> two = Flux.just(this::storeTwo);
        Session session = new SessionImpl(1);

        Flux<Action> fluxExt = one
            .filterWhen(t -> Mono.subscriberContext().map(ctx ->
                    ctx.<Session>get("session").getAttribute("zero") != null))
            .switchIfEmpty(two)
            .subscriberContext(ctx -> ctx.put("session", session));

        StepVerifier.create(fluxExt.reduce(session, this::doStep))
            .expectNextMatches(s -> s.getAttribute("two").equals(2))
            .verifyComplete();
    }

    private Session incrementRed(Session session) {
        return incrementKey(session, "red");
    }

    private Session incrementRedRetry(Session session) {
        log.debug("incrementRedRetry");
        numTries++;
        session.setAttribute("numTries", numTries);
        if (numTries == 1) {
            throw new IllegalStateException("forcing retry");
        }
        return incrementKey(session, "red");
    }

    private Session incrementBlue(Session session) {
        return incrementKey(session, "blue");
    }

    private Session incrementGreen(Session session) {
        log.debug("incrementGreen");
        return incrementKey(session, "green");
    }

    private Session incrementKey(Session session, String key) {
        Integer red = session.getAttribute(key);
        if (red == null) {
            session.setAttribute(key, 1);
        }
        else {
            session.setAttribute(key, red + 1);
        }
        return session;
    }

    @Test
    public void shouldRepeat() throws InterruptedException {
        Flux<Action> one = Flux.just(this::incrementRed);
        Flux<Action> two = Flux.just(this::incrementBlue);
        Flux<Action> green = Flux.just(this::incrementGreen);
        Session session = new SessionImpl(1);
        session.setAttribute("times", 0);

        Flux<Step> oneExt = one.map(Step::new);
        Flux<Step> twoExt = two.map(Step::new);
        Flux<Step> greenExt = green.map(Step::new);

        Step repeatStep = new Step(null);
        Repeat<Step> repeat = Repeat.<Step> onlyIf(c -> {
            log.debug("onlyIf");
            Session ses = c.applicationContext().getSession();
            if (ses == null) {
                return true;
            }
            Integer times = ses.getAttribute("blue");
            if (times == null) {
                return true;
            }
            return times < 3;
        }).withApplicationContext(repeatStep);

        Flux<Step> fluxExt = oneExt.concatWith(twoExt)
            .repeatWhen(repeat)
            .concatWith(greenExt)
            .subscriberContext(Context.of("repeatsLeft", 4));

        repeatStep.setSession(session);

        StepVerifier.create(fluxExt.reduce(session, this::doStep))
        .expectNextMatches(s ->
            s.getAttribute("red").equals(3) &&
            s.getAttribute("green").equals(1) &&
            s.getAttribute("blue").equals(3))
        .verifyComplete();
    }


    @Test
    public void shouldRepeatWithSubscriberContext() throws InterruptedException {
        Flux<Action> one = Flux.just(this::incrementRed);
        Flux<Action> two = Flux.just(this::incrementBlue);
        Flux<Action> green = Flux.just(this::incrementGreen);
        Session session = new SessionImpl(1);
        session.setAttribute("times", 0);

        Flux<Step> oneExt = one.map(Step::new);
        Flux<Step> twoExt = two.map(Step::new);
        Flux<Step> greenExt = green.map(Step::new);

        Flux<Step> fluxExt = oneExt.concatWith(twoExt)
            .repeatWhen(e -> repeater(e, "repeatsLeft"))
            .subscriberContext(ctx -> ctx.put("repeatsLeft", 4));

        fluxExt = fluxExt.concatWith(greenExt);


        StepVerifier.create(fluxExt.reduce(session, this::doStep))
        .expectNextMatches(s ->
            s.getAttribute("red").equals(5) &&
            s.getAttribute("green").equals(1) &&
            s.getAttribute("blue").equals(5))
        .verifyComplete();
    }

    private Mono<Context> decrementRepeats(Context ctx, String key) {
        int repeatsLeft = ctx.getOrDefault(key, 0);
        if (repeatsLeft > 0) {
            return Mono.just(ctx.put(key, repeatsLeft - 1));
        } else {
            return Mono.error(new IllegalStateException("no more repeats"));
        }
    }

    private Flux<Context> repeater(Flux<Long> emitted, String key) {
        return emitted.flatMap(e -> Mono.subscriberContext())
            .flatMap(ctx -> decrementRepeats(ctx, key))
            .onErrorResume(IllegalStateException.class, exc -> Mono.empty());
    }

    private Flux<Context> retrier(Flux<Throwable> emitted, String key) {
        return emitted.flatMap(e -> Mono.subscriberContext())
            .flatMap(ctx -> decrementRepeats(ctx, key))
            .onErrorResume(IllegalStateException.class, exc -> Mono.empty());
    }

    private static Session applyAction(Action action, Context context) {
        Session session = context.get("_session");
        return action.apply(session);
    }

    private static Flux<Session> applyToContextSession(Flux<Action> action) {
        return action.flatMap(a -> Mono.subscriberContext().map(ctx -> applyAction(a, ctx)));
    }

    @Test
    public void shouldRetryWithSubscriberContext() throws InterruptedException {
        Flux<Action> red = Flux.just(this::incrementRedRetry);
        Flux<Action> green = Flux.just(this::incrementGreen);
        Session session = new SessionImpl(1);

        Flux<Session> redExt = applyToContextSession(red);
        Flux<Session> greenExt = applyToContextSession(green);

        Flux<Session> fluxExt = redExt.retryWhen(e -> retrier(e, "repeatsLeft"))
            .subscriberContext(ctx -> ctx.put("repeatsLeft", 1));

        fluxExt = fluxExt.concatWith(greenExt)
            .subscriberContext(ctx -> ctx.put("_session", session));

        StepVerifier.create(fluxExt)
            .expectNextMatches(s ->
                s.getAttribute("red").equals(1) &&
                s.getAttribute("numTries").equals(2))
            .expectNextMatches(s ->
                s.getAttribute("red").equals(1) &&
                s.getAttribute("green").equals(1) &&
                s.getAttribute("numTries").equals(2))
            .verifyComplete();
    }

    @Test
    public void shouldRetryTwoStepsWithSubscriberContext() throws InterruptedException {
        Flux<Action> red = Flux.just(this::incrementRedRetry);
        Flux<Action> green = Flux.just(this::incrementGreen);
        Session session = new SessionImpl(1);

        Flux<Session> redExt = applyToContextSession(red);
        Flux<Session> greenExt = applyToContextSession(green);

        Flux<Session> fluxExt = redExt.concatWith(greenExt).retryWhen(e -> retrier(e, "repeatsLeft"))
            .subscriberContext(ctx -> ctx.put("repeatsLeft", 1));

        fluxExt = fluxExt.concatWith(greenExt)
            .subscriberContext(ctx -> ctx.put("_session", session));

        StepVerifier.create(fluxExt)
            .expectNextMatches(s ->
                s.getAttribute("red").equals(1) &&
                s.getAttribute("numTries").equals(2))
            .expectNextMatches(s ->
                s.getAttribute("red").equals(1) &&
                s.getAttribute("green").equals(1) &&
                s.getAttribute("numTries").equals(2))
            .expectNextMatches(s ->
                s.getAttribute("red").equals(1) &&
                s.getAttribute("green").equals(2) &&
                s.getAttribute("numTries").equals(2))
            .verifyComplete();
    }
}
