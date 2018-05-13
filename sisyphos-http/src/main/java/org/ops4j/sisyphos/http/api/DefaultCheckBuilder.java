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
package org.ops4j.sisyphos.http.api;

import org.hamcrest.Matcher;

/**
 * @author Harald Wellmann
 *
 */
public class DefaultCheckBuilder<R, T> implements CheckBuilder<R, T> {

    private Extractor<R, T> extractor;
    private Matcher<T> validator;
    private String saveAsKey;


    public DefaultCheckBuilder(Extractor<R, T> extractor, Matcher<T> validator, String saveAsKey) {
        this.extractor = extractor;
        this.validator = validator;
        this.saveAsKey = saveAsKey;
    }

    @Override
    public CheckBuilder<R, T> saveAs(String saveAsKey) {
        this.saveAsKey = saveAsKey;
        return this;
    }

    @Override
    public Check<R, T> build() {
        return new DefaultCheck<>(extractor, validator, saveAsKey);
    }
}
