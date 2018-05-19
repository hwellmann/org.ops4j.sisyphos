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
 * Adds additional request information or response checks to the HTTP request under construction.
 *
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

    /**
     * Adds a query parameter with the given name and value.
     *
     * @param name
     *            parameter name
     * @param value
     *            parameter value
     * @return this builder
     */
    public HttpRequestBuilder queryParam(String name, String value) {
        uriBuilder.queryParam(name, value);
        return this;
    }

    /**
     * Adds a form parameter with the given name and value.
     *
     * @param name
     *            parameter name
     * @param value
     *            parameter value
     * @return this builder
     */
    public HttpRequestBuilder formParam(String name, String value) {
        form.param(name, value);
        return this;
    }

    /**
     * Adds a header with the given name and value.
     *
     * @param name
     *            header name
     * @param value
     *            header value
     * @return this builder
     */
    public HttpRequestBuilder header(String name, String value) {
        headers.add(name, value);
        return this;
    }

    /**
     * Adds a header with the given name and value extracted from the current session by the given
     * function.
     *
     * @param name
     *            header name
     * @param attr
     *            string-valued session function
     * @return this builder
     */
    public HttpRequestBuilder header(String name, Function<Session, String> attr) {
        headers.add(name, attr);
        return this;
    }

    /**
     * Adds a string-valued body entity to the request.
     * @param body body definition
     * @return this builder
     */
    public HttpRequestBuilder body(HttpBody<String> body) {
        this.body = body;
        return this;
    }

    /**
     * Adds the given response checks to the request.
     * @param checks list of response checks
     * @return this builder
     */
    @SafeVarargs
    public final HttpRequestBuilder check(CheckBuilder<Response, ?>... checks) {
        checkBuilders = checkBuilders.appendAll(List.of(checks));
        return this;
    }

    /**
     * Gets the HTTP method name.
     * @return method name
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Gets the HTTP headers.
     * @return multivalued header map
     */
    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    /**
     * Gets the URI builder for this request.
     * @return URI builder
     */
    public UriBuilder getUriBuilder() {
        return uriBuilder;
    }

    /**
     * Gets the form entity for this request.
     * @return form entity, or null, if no form is defined
     */
    public Form getForm() {
        return form;
    }

    /**
     * Gets the checks to be applied to the response.
     * @return list of check builders
     */
    public List<CheckBuilder<Response, ?>> getCheckBuilders() {
        return checkBuilders;
    }

    /**
     * Gets the body entity.
     * @return body entity
     */
    public HttpBody<String> getBody() {
        return body;
    }

    /**
     * Gets the request name to be used in reports.
     * @return request name
     */
    public String getRequestName() {
        return requestName;
    }

    /**
     * Gets the (relative) URL of this request.
     * @return URL
     */
    public String getUrl() {
        return url;
    }
}
