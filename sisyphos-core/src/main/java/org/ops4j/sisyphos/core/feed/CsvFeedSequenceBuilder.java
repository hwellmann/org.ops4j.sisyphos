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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.ops4j.sisyphos.api.feed.CsvFeedBuilder;
import org.ops4j.sisyphos.core.builder.ScenarioContext;

import io.vavr.collection.IndexedSeq;
import io.vavr.collection.Iterator;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import io.vavr.control.Try;

public class CsvFeedSequenceBuilder extends RecordFeedSequenceBuilder<String> {


    private CsvFeedBuilder csvFeed;

    public CsvFeedSequenceBuilder(CsvFeedBuilder csvFeed) {
        super(csvFeed.getStrategy());
        this.csvFeed = csvFeed;
    }

    @Override
    public IndexedSeq<Map<String, String>> generateRecords(ScenarioContext context) {
        File csvFile = new File(context.getDataPath(), csvFeed.getName());
        return Try.withResources(() -> new FileInputStream(csvFile)).of(this::readRecords).get();
    }

    private IndexedSeq<Map<String, String>> readRecords(InputStream is) throws IOException {
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        return Vector.ofAll(Iterator.ofAll(parser).map(this::convert));
    }

    private Map<String, String> convert(CSVRecord csv) {
        return LinkedHashMap.ofAll(csv.toMap());
    }
}
