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

import java.util.concurrent.atomic.AtomicInteger;

import org.ops4j.sisyphos.api.feed.FeedStrategy;
import org.ops4j.sisyphos.core.common.FeedSequenceBuilder;
import org.ops4j.sisyphos.core.common.ScenarioContext;

import io.vavr.collection.Iterator;
import io.vavr.collection.Map;

/**
 * @author Harald Wellmann
 *
 */
public abstract class RecordFeedSequenceBuilder<T> implements FeedSequenceBuilder<T> {

    private FeedStrategy strategy;
    private Iterator<Map<String, T>> feed;
    private AtomicInteger init = new AtomicInteger(0);

    public RecordFeedSequenceBuilder(FeedStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Iterator<Map<String, T>> buildFeed(ScenarioContext context) {
        if (init.getAndIncrement() == 0) {
            feed = strategy.createFeed(generateRecords(context));
        }
        return feed;
    }
}
