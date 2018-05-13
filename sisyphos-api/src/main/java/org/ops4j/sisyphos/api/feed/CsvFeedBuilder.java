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

/**
 * @author Harald Wellmann
 *
 */
public class CsvFeedBuilder implements FeedBuilder<String> {

    private FeedStrategy strategy;
    private String fileName;

    /**
     * @param records
     * @param strategy
     */
    public CsvFeedBuilder(String fileName, FeedStrategy strategy) {
        this.strategy = strategy;
        this.fileName = fileName;
    }

    public CsvFeedBuilder(String fileName) {
        this(fileName, FeedStrategy.QUEUE);
    }


    @Override
    public FeedStrategy getStrategy() {
        return strategy;
    }

    @Override
    public String getName() {
        return fileName;
    }
}
