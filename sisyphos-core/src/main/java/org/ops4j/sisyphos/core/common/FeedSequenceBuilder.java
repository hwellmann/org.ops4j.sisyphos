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
package org.ops4j.sisyphos.core.common;

import io.vavr.collection.IndexedSeq;
import io.vavr.collection.Iterator;
import io.vavr.collection.Map;

/**
 * Builds a feed from a feed builder definition.
 *
 * @author Harald Wellmann
 *
 */
public interface FeedSequenceBuilder<T> {

    /**
     * Generates a sequence of records which will yield the feed, subject to a feed strategy.
     *
     * @param context
     *            scenario context
     * @return record sequence
     */
    IndexedSeq<Map<String, T>> generateRecords(ScenarioContext context);

    /**
     * Builds a possibly infinite feed of records from a given finite sequence and a feed strategy.
     *
     * @param context
     *            scenario context
     * @return record iterator
     */
    Iterator<Map<String, T>> buildFeed(ScenarioContext context);
}
