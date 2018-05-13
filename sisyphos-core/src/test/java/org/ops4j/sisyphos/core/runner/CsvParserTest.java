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
package org.ops4j.sisyphos.core.runner;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * @author Harald Wellmann
 *
 */
public class CsvParserTest {

    @Test
    public void shouldParseCsv() throws IOException {
        try (InputStream is = new FileInputStream("src/test/resources/data/login.csv");
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertThat(headerMap)
                .containsEntry("login", 0)
                .containsEntry("password", 1)
                .containsEntry("numLogins", 2);

            AtomicInteger index = new AtomicInteger(1);
            parser.forEach(record -> verifyRecord(record, index));
        }
    }

    private void verifyRecord(CSVRecord record, AtomicInteger index) {
        int i = index.getAndIncrement();
        assertThat(record).isNotNull();

        assertThat(record.toMap())
            .containsEntry("login", "user" + i)
            .containsEntry("password", "secret" + i)
            .containsEntry("numLogins", Integer.toString(i));
    }
}
