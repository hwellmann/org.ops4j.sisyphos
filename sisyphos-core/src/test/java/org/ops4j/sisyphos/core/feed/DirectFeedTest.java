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
package org.ops4j.sisyphos.core.feed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.ops4j.sisyphos.api.feed.FeedBuilder;
import org.ops4j.sisyphos.api.feed.FeedStrategy;
import org.ops4j.sisyphos.api.simulation.Sisyphos;

import io.vavr.Tuple2;
import io.vavr.collection.Iterator;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

/**
 * @author Harald Wellmann
 *
 */
public class DirectFeedTest {

    private static <T> Iterator<Map<String, T>> buildFeed(FeedBuilder<T> feed) {
        FeedSequenceBuilderAdapter<T> adapter = new FeedSequenceBuilderAdapter<T>();
        return adapter.adapt(feed).buildFeed(null);
    }


    @Test
    public void shouldConsumeQueue() {
        Iterator<Map<String, Integer>> feed = buildFeed(Sisyphos.feedOf("foo", FeedStrategy.QUEUE, 2, 3, 5));
        assertThat((Object) feed).isNotNull();

        assertThat(feed.hasNext()).isTrue();
        assertThat(feed.next()).containsExactlyElementsOf(LinkedHashMap.of("foo", 2));

        assertThat(feed.hasNext()).isTrue();
        assertThat(feed.next()).containsExactlyElementsOf(LinkedHashMap.of("foo", 3));

        assertThat(feed.hasNext()).isTrue();
        assertThat(feed.next()).containsExactlyElementsOf(LinkedHashMap.of("foo", 5));

        assertThat(feed.hasNext()).isFalse();
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> feed.next());
    }

    @Test
    public void shouldConsumeSingletonQueue() {
        Iterator<Map<String, Integer>> feed = buildFeed(Sisyphos.feedOf("foo", FeedStrategy.QUEUE, 2));
        assertThat((Object) feed).isNotNull();

        assertThat(feed.hasNext()).isTrue();
        assertThat(feed.next()).containsExactlyElementsOf(LinkedHashMap.of("foo", 2));

        assertThat(feed.hasNext()).isFalse();
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> feed.next());
    }

    @Test
    public void shouldConsumeEmptyQueue() {
        Iterator<Map<String, Integer>> feed = buildFeed(Sisyphos.feedOf("foo", FeedStrategy.QUEUE));
        assertThat((Object) feed).isNotNull();

        assertThat(feed.hasNext()).isFalse();
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> feed.next());
    }

    @Test
    public void shouldConsumeCircular() {
        Iterator<Map<String, Integer>> feed = buildFeed(Sisyphos.feedOf("foo", FeedStrategy.CIRCULAR, 2, 3, 5));
        assertThat((Object) feed).isNotNull();

        for (int i = 0; i < 20; i++) {
            assertThat(feed.hasNext()).isTrue();
            assertThat(feed.next()).containsExactlyElementsOf(LinkedHashMap.of("foo", 2));

            assertThat(feed.hasNext()).isTrue();
            assertThat(feed.next()).containsExactlyElementsOf(LinkedHashMap.of("foo", 3));

            assertThat(feed.hasNext()).isTrue();
            assertThat(feed.next()).containsExactlyElementsOf(LinkedHashMap.of("foo", 5));
        }
    }

    @Test
    public void shouldConsumeRandom() {
        Iterator<Map<String, Integer>> feed = buildFeed(Sisyphos.feedOf("foo", FeedStrategy.CIRCULAR, 2, 3, 5));
        assertThat((Object) feed).isNotNull();

        for (int i = 0; i < 60; i++) {
            assertThat(feed.hasNext()).isTrue();

            Map<String, Integer> record = feed.next();

            assertThat(record).hasSize(1);
            assertThat(record.get("foo").get()).isIn(2, 3, 5);
        }
    }

    @Test
    public void shouldConsumeShuffle() {
        Iterator<Map<String, Integer>> feed = buildFeed(Sisyphos.feedOf("foo", FeedStrategy.SHUFFLE, 2, 3, 5));
        assertThat((Object) feed).isNotNull();

        List<Tuple2<String, Integer>> tuples = feed.flatMap(item -> item.iterator()).toList();
        assertThat(tuples.map(t -> t._1()).toSet()).containsExactly("foo");
        assertThat(tuples.map(t -> t._2()).toSet()).containsExactly(2, 3, 5);
    }

    @Test
    public void roundRobin() {
        AtomicInteger robin = new AtomicInteger(0);
        assertThat(List.range(0, 10).map(i -> robin.getAndUpdate(x -> (x + 1) % 3)))
            .containsExactly(0, 1, 2, 0, 1, 2, 0, 1, 2, 0);
    }
}
