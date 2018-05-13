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
 * @author Harald Wellmann
 *
 */
public class HttpMethodBuilder {

    private String name;

    public HttpMethodBuilder(String name) {
        this.name = name;
    }

    public HttpRequestBuilder get(String url) {
        return new HttpRequestBuilder(name, "GET", url);
    }

    public HttpRequestBuilder post(String url) {
        return new HttpRequestBuilder(name, "POST", url);
    }

    public HttpRequestBuilder put(String url) {
        return new HttpRequestBuilder(name, "PUT", url);
    }

    public HttpRequestBuilder delete(String url) {
        return new HttpRequestBuilder(name, "DELETE", url);
    }

    public HttpRequestBuilder patch(String url) {
        return new HttpRequestBuilder(name, "PATCH", url);
    }

    public HttpRequestBuilder head(String url) {
        return new HttpRequestBuilder(name, "HEAD", url);
    }

    public HttpRequestBuilder options(String url) {
        return new HttpRequestBuilder(name, "OPTIONS", url);
    }
}
