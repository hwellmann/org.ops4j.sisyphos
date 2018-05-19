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
 * Builders for HTTP requests and related checks.
 * <p>
 * Extends the domain specific language for Sisyphos defined in the {@code sisyphos-api} module.
 *
 * @author Harald Wellmann
 *
 */
public final class SisyphosHttp {

    private SisyphosHttp() {
        // hidden constructor
    }

    /**
     * Builds an HTTP request with the given request name.
     *
     * @param name
     *            request name appearing in reports
     * @return builder
     */
    public static HttpMethodBuilder http(String name) {
        return new HttpMethodBuilder(name);
    }

    /**
     * Creates an HTTP body entity from a template file at the given path.
     * <p>
     * The path is relative to the template path defined by configuration.
     * <p>
     * Variables in the template will be interpolated with values from the current session.
     *
     * @param path
     *            template path
     * @return builder
     */
    public static TemplateBody template(String path) {
        return new TemplateBody(path);
    }

    /**
     * Creates an HTTP body entity using the given content.
     *
     * @param content
     *            literal body content
     * @return builder
     */
    public static RawBody raw(String content) {
        return new RawBody(content);
    }

    /**
     * Creates a JSON path check for the given JSON path.
     *
     * @param jsonPath
     *            JSON path expression
     * @param <T>
     *            type of extracted value
     * @return builder
     */
    public static <T> JsonPathCheckBuilder<T> jsonPath(String jsonPath) {
        return new JsonPathCheckBuilder<>(jsonPath);
    }

    /**
     * Creates an HTTP status check.
     *
     * @return builder
     */
    public static DefaultValidatorCheckBuilder<Response, Integer> status() {
        return new StatusCheckBuilder();
    }

    /**
     * Creates an HTTP configuration builder.
     *
     * @return builder
     */
    public static HttpConfiguration httpConfig() {
        return new HttpConfiguration();
    }
}
