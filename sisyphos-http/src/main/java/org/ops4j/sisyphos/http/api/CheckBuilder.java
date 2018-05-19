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
 * Interface to be implemented by all check builders.
 *
 * @param <R>
 *            response type
 * @param <T>
 *            type of extracted value to be checked
 *
 * @author Harald Wellmann
 */
public interface CheckBuilder<R, T> {

    /**
     * Saves the value extracted by the current check as a session attribute with the given key.
     * @param key session key
     * @return this builder
     */
    CheckBuilder<R, T> saveAs(String key);

    /**
     * Builds a check.
     * @return check
     */
    Check<R, T> build();
}
