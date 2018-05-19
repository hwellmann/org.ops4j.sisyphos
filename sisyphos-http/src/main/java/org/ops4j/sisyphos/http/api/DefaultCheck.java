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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.ops4j.sisyphos.api.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Harald Wellmann
 *
 */
public class DefaultCheck<R, T> implements Check<R, T> {

    private static Logger log = LoggerFactory.getLogger(DefaultCheck.class);

    private Extractor<R, T> extractor;
    private Matcher<T> validator;
    private String saveAsKey;

    public DefaultCheck(Extractor<R,T> extractor, Matcher<T> validator, String saveAsKey) {
        this.extractor = extractor;
        this.validator = validator;
        this.saveAsKey = saveAsKey;
    }

    @Override
    public Extractor<R, T> extractor() {
        return extractor;
    }

    @Override
    public Matcher<T> validator() {
        return validator;
    }

    @Override
    public String saveAs() {
        return saveAsKey;
    }

    @Override
    public void check(R response, Session session) {
        T actual = extractor().apply(response);
        if (!validator().matches(actual)) {
            logError(session, actual);
        }
        if (saveAs() != null) {
            session.setAttribute(saveAs(), actual);
        }
    }

    private void logError(Session session, T actual) {
        Description description = new StringDescription();
        description.appendText("Expected ");
        validator.describeTo(description);
        description.appendText(" but ");
        validator.describeMismatch(actual, description);
        log.error(description.toString());
        session.markAsFailed();
        session.setAttribute("_message", description.toString());
    }
}
