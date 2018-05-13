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

import java.util.function.Function;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.ops4j.sisyphos.api.action.ActionBuilder;
import org.ops4j.sisyphos.api.session.Session;

import io.vavr.collection.List;

/**
 * @author Harald Wellmann
 *
 */
public class HttpRequestBuilder implements ActionBuilder {

    private String methodName;
    private UriBuilder uriBuilder;
    private MultivaluedMap<String, Object> headers;
    private Form form;
    private List<CheckBuilder<Response, ?>> checkBuilders;
    private HttpBody<String> body;
    private String requestName;
    private String url;

    HttpRequestBuilder(String requestName, String methodName, String url) {
        this.requestName = requestName;
        this.methodName = methodName;
        this.url = url;
        this.headers = new MultivaluedHashMap<>();
        this.form = new Form();
        this.checkBuilders = List.empty();
    }

    public HttpRequestBuilder queryParam(String name, String value) {
        uriBuilder.queryParam(name, value);
        return this;
    }

    public HttpRequestBuilder formParam(String name, String value) {
        form.param(name, value);
        return this;
    }

    public HttpRequestBuilder header(String name, String value) {
        headers.add(name, value);
        return this;
    }

    public HttpRequestBuilder header(String name, Function<Session, String> attr) {
        headers.add(name, attr);
        return this;
    }

    public HttpRequestBuilder body(HttpBody<String> body) {
        this.body = body;
        return this;
    }

    @SafeVarargs
    public final HttpRequestBuilder check(CheckBuilder<Response, ?>... checks) {
        checkBuilders = checkBuilders.appendAll(List.of(checks));
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public UriBuilder getUriBuilder() {
        return uriBuilder;
    }

    public Form getForm() {
        return form;
    }

    public List<CheckBuilder<Response, ?>> getCheckBuilders() {
        return checkBuilders;
    }

    public HttpBody<String> getBody() {
        return body;
    }

    public String getRequestName() {
        return requestName;
    }

    public String getUrl() {
        return url;
    }
}
