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

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.not;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * @author Harald Wellmann
 *
 */
public class DefaultValidatorCheckBuilder<R, T> implements ValidatorCheckBuilder<R, T> {


    private Extractor<R, T> extractor;

    public DefaultValidatorCheckBuilder(Extractor<R, T> extractor) {
        this.extractor = extractor;
    }

    @Override
    public CheckBuilder<R, T> is(T expected) {
        return new DefaultCheckBuilder<>(extractor, Matchers.is(expected), null);
    }

    @Override
    public CheckBuilder<R, T> isNot(T expected) {
        return new DefaultCheckBuilder<>(extractor, not(expected), null);
    }

    @Override
    @SafeVarargs
    public final CheckBuilder<R, T> in(T... expected) {

        return new DefaultCheckBuilder<R, T>(extractor, isOneOf(expected), null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CheckBuilder<R, T> saveAs(String key) {
        return new DefaultCheckBuilder<R, T>(extractor, (Matcher<T>) anything(), key);
    }
}
