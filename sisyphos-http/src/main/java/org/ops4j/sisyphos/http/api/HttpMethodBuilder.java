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
 * Builds an HTTP request with a given HTTP method, URL and request name.
 * <p>
 * If the given URL is relative, it will be resolved with respect to the base URL defined in the
 * HTTP configuration.
 *
 * @author Harald Wellmann
 *
 */
public class HttpMethodBuilder {

    private String name;

    /**
     * Creates a request builder with the given request name.
     *
     * @param name
     *            request name
     */
    public HttpMethodBuilder(String name) {
        this.name = name;
    }

    /**
     * Builds a {@code GET} request for the given URL.
     *
     * @param url URL
     * @return builder
     */
    public HttpRequestBuilder get(String url) {
        return new HttpRequestBuilder(name, "GET", url);
    }

    /**
     * Builds a {@code POST} request for the given URL.
     *
     * @param url URL
     * @return builder
     */
    public HttpRequestBuilder post(String url) {
        return new HttpRequestBuilder(name, "POST", url);
    }

    /**
     * Builds a {@code PUT} request for the given URL.
     *
     * @param url URL
     * @return builder
     */
    public HttpRequestBuilder put(String url) {
        return new HttpRequestBuilder(name, "PUT", url);
    }

    /**
     * Builds a {@code DELETE} request for the given URL.
     *
     * @param url URL
     * @return builder
     */
    public HttpRequestBuilder delete(String url) {
        return new HttpRequestBuilder(name, "DELETE", url);
    }

    /**
     * Builds a {@code PATCH} request for the given URL.
     *
     * @param url URL
     * @return builder
     */
    public HttpRequestBuilder patch(String url) {
        return new HttpRequestBuilder(name, "PATCH", url);
    }

    /**
     * Builds a {@code HEAD} request for the given URL.
     *
     * @param url URL
     * @return builder
     */
    public HttpRequestBuilder head(String url) {
        return new HttpRequestBuilder(name, "HEAD", url);
    }

    /**
     * Builds a {@code OPTIONS} request for the given URL.
     *
     * @param url URL
     * @return builder
     */
    public HttpRequestBuilder options(String url) {
        return new HttpRequestBuilder(name, "OPTIONS", url);
    }
}
