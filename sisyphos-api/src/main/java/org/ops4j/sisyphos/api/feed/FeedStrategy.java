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
 * @author Harald Wellmann
 *
 */
public enum FeedStrategy {

    QUEUE {
        @Override
        public <T> Iterator<Map<String, T>> createFeed(IndexedSeq<Map<String, T>> records) {
            return records.iterator();
        }
    },


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

    SHUFFLE {
        @Override
        public <T> Iterator<Map<String, T>> createFeed(IndexedSeq<Map<String, T>> records) {
            return records.shuffle().iterator();
        }
    },

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

    public abstract <T> Iterator<Map<String, T>>
        createFeed(IndexedSeq<Map<String, T>> records);
}
