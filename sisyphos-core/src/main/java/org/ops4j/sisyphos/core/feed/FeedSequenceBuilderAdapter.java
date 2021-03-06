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
package org.ops4j.sisyphos.core.feed;

import org.ops4j.sisyphos.api.feed.CsvFeedBuilder;
import org.ops4j.sisyphos.api.feed.DirectFeedBuilder;
import org.ops4j.sisyphos.api.feed.FeedBuilder;
import org.ops4j.sisyphos.core.common.Adapter;
import org.ops4j.sisyphos.core.common.FeedSequenceBuilder;

public class FeedSequenceBuilderAdapter<F> implements Adapter<FeedBuilder<F>, FeedSequenceBuilder<F>> {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public FeedSequenceBuilder<F> adapt(FeedBuilder<F> feed) {
        if (feed instanceof CsvFeedBuilder) {
            return (FeedSequenceBuilder<F>) new CsvFeedSequenceBuilder((CsvFeedBuilder) feed);
        }
        if (feed instanceof DirectFeedBuilder) {
            return new DirectFeedSequenceBuilder<F>((DirectFeedBuilder) feed);
        }
        throw new IllegalArgumentException(feed.getClass().getName());
    }
}
