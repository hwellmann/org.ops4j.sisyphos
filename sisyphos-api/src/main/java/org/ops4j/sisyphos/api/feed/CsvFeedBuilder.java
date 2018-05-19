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
 * Builds a feed from a CSV file. The name of this feed is equal to the file name.
 *
 * @author Harald Wellmann
 *
 */
public class CsvFeedBuilder implements FeedBuilder<String> {

    private FeedStrategy strategy;
    private String fileName;

    /**
     * Creates a feed builder from the given CSV file with the given strategy.
     * @param fileName CSV file name
     * @param strategy feed stratey
     */
    public CsvFeedBuilder(String fileName, FeedStrategy strategy) {
        this.strategy = strategy;
        this.fileName = fileName;
    }

    /**
     * Creates a feed builder from the given CSV file with the queue strategy.
     * @param fileName CSV file name
     */
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
