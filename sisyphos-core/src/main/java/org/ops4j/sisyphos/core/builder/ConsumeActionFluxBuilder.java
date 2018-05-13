/*
 * Copyright 2018 OPS4J Contributors
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
package org.ops4j.sisyphos.core.builder;

import org.ops4j.sisyphos.api.action.ConsumeActionBuilder;
import org.ops4j.sisyphos.api.feed.FeedBuilder;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.core.common.ScenarioContext;
import org.ops4j.sisyphos.core.feed.FeedSequenceBuilderAdapter;

import io.vavr.collection.HashMap;
import io.vavr.collection.Iterator;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import reactor.core.publisher.Flux;

public class ConsumeActionFluxBuilder<F> implements FluxBuilder {

    private static Map<String, Iterator<Map<String, ?>>> feedMap = HashMap.empty();

    private ConsumeActionBuilder<F> consumeActionBuilder;

    public ConsumeActionFluxBuilder(ConsumeActionBuilder<F> consumeActionBuilder) {
        this.consumeActionBuilder = consumeActionBuilder;
    }

    @Override
    public Flux<Session> buildFlux(ScenarioContext context) {
        FeedBuilder<F> feedHandle = consumeActionBuilder.getFeedBuilder();

        Iterator<Map<String, F>> feed = null;
        Option<Iterator<Map<String, ?>>> optFeed = feedMap.get(feedHandle.getName());
        if (optFeed.isEmpty()) {
            FeedSequenceBuilderAdapter<F> adapter = new FeedSequenceBuilderAdapter<F>();
            feed = adapter.adapt(feedHandle).buildFeed(context);
            feedMap = feedMap.put(feedHandle.getName(), adapt(feed));
        }
        else {
            feed = adapt(optFeed.get());
        }
        return applyToContextSession(Flux.just(toAction(feed)));
    }

    @SuppressWarnings("unchecked")
    private <T, U> Iterator<T> adapt(Iterator<U> iterator) {
        return (Iterator<T>) iterator;
    }

    private ConsumeAction<F> toAction(Iterator<Map<String, F>> feed) {
        return new ConsumeAction<F>(feed);
    }
}
