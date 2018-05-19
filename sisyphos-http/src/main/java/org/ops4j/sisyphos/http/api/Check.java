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
import org.ops4j.sisyphos.api.session.Session;

/**
 * Interface to be implemented by all response checks.
 *
 * @param <R>
 *            response type
 * @param <T>
 *            type of extracted value to be checked
 *
 * @author Harald Wellmann
 *
 */
public interface Check<R, T> {

    /**
     * Gets the value extractor for this check.
     *
     * @return extractor
     */
    Extractor<R, T> extractor();

    /**
     * Gets a validator for the extracted value.
     *
     * @return validator
     */
    Matcher<T> validator();

    /**
     * Gets the attribute name under which the extracted value will be saved in the current session.
     *
     * @return attribute name
     */
    String saveAs();

    /**
     * Applies this check to the given response and the given session.
     * <p>
     * The session is marked as failed if the check fails.
     *
     * @param response
     *            current response
     * @param session
     *            current session
     */
    void check(R response, Session session);
}
