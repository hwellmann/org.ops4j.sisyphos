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

/**
 * @author Harald Wellmann
 *
 */
public class Dsl {

    public static HttpMethodBuilder http(String name) {
        return new HttpMethodBuilder(name);
    }

    public static TemplateBody template(String path) {
        return new TemplateBody(path);
    }

    public static RawBody raw(String content) {
        return new RawBody(content);
    }

    public static <T> JsonPathCheckBuilder<T> jsonPath(String jsonPath) {
        return new JsonPathCheckBuilder<>(jsonPath);
    }

    public static DefaultValidatorCheckBuilder<Response, Integer> status() {
        return new StatusCheckBuilder();
    }

    public static HttpConfiguration httpConfig() {
        return new HttpConfiguration();
    }
}
