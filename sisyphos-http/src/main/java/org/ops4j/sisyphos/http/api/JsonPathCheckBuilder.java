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

import javax.ws.rs.core.Response;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Harald Wellmann
 *
 */
public class JsonPathCheckBuilder<T> implements FindCheckBuilder<Response, T> {

    private String jsonPath;

    public JsonPathCheckBuilder(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    @Override
    public ValidatorCheckBuilder<Response, T> find() {
        return new DefaultValidatorCheckBuilder<>(this::extract);
    }

    public T extract(Response response) {
        String json = response.readEntity(String.class);
        DocumentContext context = JsonPath.parse(json);
        return context.read(jsonPath);
    }
}
