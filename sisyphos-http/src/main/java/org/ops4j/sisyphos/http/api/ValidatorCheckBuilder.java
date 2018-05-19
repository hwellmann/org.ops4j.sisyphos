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

/**
 * Builder used in fluent syntax for checks.
 *
 * @param <R>
 *            response type
 * @param <T>
 *            type of extracted value to be checked
 *
 * @author Harald Wellmann
 *
 */
public interface ValidatorCheckBuilder<R, T> {

    /**
     * Checks that the extracted value is equal to the expected value.
     *
     * @param expected
     *            expected value
     * @return builder
     */
    CheckBuilder<R, T> is(T expected);

    /**
     * Checks that the extracted value is not equal to the expected value.
     *
     * @param unexpected
     *            unexpected value
     * @return builder
     */
    CheckBuilder<R, T> isNot(T unexpected);

    /**
     * Checks that the extracted value is equal to one of the accepted values.
     *
     * @param expected
     *            list of accepted values
     * @return builder
     */
    @SuppressWarnings("unchecked")
    CheckBuilder<R, T> in(T... expected);

    /**
     * Defines the attribute name under which the extracted value will be stored in the current
     * session.
     *
     * @param key
     *            attribute name
     * @return builder
     */
    CheckBuilder<R, T> saveAs(String key);

}
