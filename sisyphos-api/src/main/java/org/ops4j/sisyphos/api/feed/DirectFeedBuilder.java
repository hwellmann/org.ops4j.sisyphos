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

import io.vavr.collection.IndexedSeq;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

/**
 * Builds a feed from a list of values.
 *
 * @author Harald Wellmann
 *
 */
public class DirectFeedBuilder<T> implements FeedBuilder<T> {

    private FeedStrategy strategy;
    private IndexedSeq<Map<String, T>> records;
    private String propertyName;

    /**
     * Creates a feed builder from a list of values.
     *
     * @param propertyName
     *            attribute name for feed values in the current session.
     * @param strategy
     *            feed strategy
     * @param values
     *            list of values
     */
    public DirectFeedBuilder(String propertyName, FeedStrategy strategy, T[] values) {
        this.strategy = strategy;
        this.propertyName = propertyName;
        records = List.of(values).map(v -> toMap(propertyName, v)).toVector();

    }

    @Override
    public FeedStrategy getStrategy() {
        return strategy;
    }

    @Override
    public String getName() {
        return propertyName;
    }

    /**
     * Gets a sequence of record from the feed values. Each record is a map with a single entry,
     * mapping the property name to one of the feed values.
     * @return record sequence
     */
    public IndexedSeq<Map<String, T>> getRecords() {
        return records;
    }

    private Map<String, T> toMap(String propertyName, T value) {
        return LinkedHashMap.of(propertyName, value);
    }
}
