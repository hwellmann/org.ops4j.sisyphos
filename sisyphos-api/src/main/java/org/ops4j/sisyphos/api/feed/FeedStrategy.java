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
package org.ops4j.sisyphos.api.feed;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.vavr.collection.IndexedSeq;
import io.vavr.collection.Iterator;
import io.vavr.collection.Map;

/**
 * A feed strategy creates a possible infinite feed from a given finite sequence of records.
 *
 * @author Harald Wellmann
 *
 */
public enum FeedStrategy {

    /**
     * A feed with queue strategy simply returns the given records in the given order and then
     * terminates.
     */
    QUEUE {

        @Override
        public <T> Iterator<Map<String, T>> createFeed(IndexedSeq<Map<String, T>> records) {
            return records.iterator();
        }
    },

    /**
     * A feed with random strategy is infinite, returning a random record from the given sequence at
     * each request.
     */
    RANDOM {

        @Override
        public <T> Iterator<Map<String, T>> createFeed(IndexedSeq<Map<String, T>> records) {
            return new Iterator<Map<String, T>>() {

                private Random random = new Random();

                @Override
                public boolean hasNext() {
                    return !records.isEmpty();
                }

                @Override
                public Map<String, T> next() {
                    return records.get(random.nextInt(records.size()));
                }
            };
        }
    },

    /**
     * A feed with shuffle strategy returns the given records in random order and then
     * terminates. Each record appears in the feed once and only once.
     */
    SHUFFLE {

        @Override
        public <T> Iterator<Map<String, T>> createFeed(IndexedSeq<Map<String, T>> records) {
            return records.shuffle().iterator();
        }
    },

    /**
     * A feed with circular strategy is infinite, returning the records from the given sequence in the given order,
     * restarting at the start of the sequence when the end is reached.
     */
    CIRCULAR {

        @Override
        public <T> Iterator<Map<String, T>> createFeed(IndexedSeq<Map<String, T>> records) {
            return new Iterator<Map<String, T>>() {

                private AtomicInteger index = new AtomicInteger(0);

                @Override
                public boolean hasNext() {
                    return !records.isEmpty();
                }

                @Override
                public Map<String, T> next() {
                    int modulus = records.size();
                    int next = index.getAndUpdate(x -> (x + 1) % modulus);
                    return records.get(next);
                }
            };
        }
    };

    public abstract <T> Iterator<Map<String, T>> createFeed(IndexedSeq<Map<String, T>> records);
}
