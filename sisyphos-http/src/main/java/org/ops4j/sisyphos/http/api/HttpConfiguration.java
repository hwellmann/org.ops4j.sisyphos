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

import org.ops4j.sisyphos.api.action.ProtocolConfiguration;

import io.vavr.control.Option;

/**
 * Builds the HTTP protocol configuration to be used by all HTTP requests.
 * <p>
 * Some properties may be overridden by settings defined per request.
 *
 * @author Harald Wellmann
 *
 */
public class HttpConfiguration implements ProtocolConfiguration {

    private String baseUrl;

    private String acceptHeader;

    private String acceptCharsetHeader;

    private String acceptEncodingHeader;

    private String acceptLanguageHeader;

    private String contentTypeHeader;

    private String userAgentHeader;

    private String username;

    private String password;

    HttpConfiguration() {
    }

    /**
     * Defines the base URL for all requests with a relative URL.
     *
     * @param baseUrl
     *            base URL
     * @return builder
     */
    public HttpConfiguration baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * Defines the value of the {@code Accept} header.
     *
     * @param acceptHeader
     *            accepted content types
     * @return builder
     */
    public HttpConfiguration acceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
        return this;
    }

    /**
     * Defines the value of the {@code Accept-Charset} header.
     *
     * @param acceptCharsetHeader
     *            accepted character set
     * @return builder
     */
    public HttpConfiguration acceptCharsetHeader(String acceptCharsetHeader) {
        this.acceptCharsetHeader = acceptCharsetHeader;
        return this;
    }

    /**
     * Defines the value of the {@code Accept-Language} header.
     *
     * @param acceptLanguageHeader
     *            accepted languages
     * @return builder
     */
    public HttpConfiguration acceptLanguageHeader(String acceptLanguageHeader) {
        this.acceptLanguageHeader = acceptLanguageHeader;
        return this;
    }

    /**
     * Defines the value of the {@code Accept-Encoding} header.
     *
     * @param acceptEncodingHeader
     *            accepted encodings
     * @return builder
     */
    public HttpConfiguration acceptEncodingHeader(String acceptEncodingHeader) {
        this.acceptEncodingHeader = acceptEncodingHeader;
        return this;
    }

    /**
     * Defines the value of the {@code Content-Type} header.
     *
     * @param contentTypeHeader
     *            content type of request body
     * @return builder
     */
    public HttpConfiguration contentTypeHeader(String contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
        return this;
    }

    /**
     * Defines the value of the {@code User-Agent} header.
     *
     * @param userAgentHeader
     *            user agent
     * @return builder
     */
    public HttpConfiguration userAgentHeader(String userAgentHeader) {
        this.userAgentHeader = userAgentHeader;
        return this;
    }

    /**
     * Defines basic authentication credentials to be used for all requests.
     *
     * @param username username
     * @param password password
     * @return builder
     */
    public HttpConfiguration basicAuth(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * Gets the base URL.
     *
     * @return the baseUrl
     */
    public Option<String> getBaseUrl() {
        return Option.of(baseUrl);
    }

    /**
     * Gets the {@code Accept} header.
     *
     * @return the acceptHeader
     */
    public Option<String> getAcceptHeader() {
        return Option.of(acceptHeader);
    }

    /**
     * Gets the {@code Accept-Charset} header.
     *
     * @return the acceptCharsetHeader
     */
    public Option<String> getAcceptCharsetHeader() {
        return Option.of(acceptCharsetHeader);
    }

    /**
     * Gets the {@code Accept-Encoding} header.
     *
     * @return the acceptEncodingHeader
     */
    public Option<String> getAcceptEncodingHeader() {
        return Option.of(acceptEncodingHeader);
    }

    /**
     * Gets the {@code Accept-Language} header.
     *
     * @return the acceptLanguageHeader
     */
    public Option<String> getAcceptLanguageHeader() {
        return Option.of(acceptLanguageHeader);
    }

    /**
     * Gets the {@code Content-Type} header.
     *
     * @return the contentTypeHeader
     */
    public Option<String> getContentTypeHeader() {
        return Option.of(contentTypeHeader);
    }

    /**
     * Gets the {@code User-Agent} header.
     *
     * @return the userAgentHeader
     */
    public Option<String> getUserAgentHeader() {
        return Option.of(userAgentHeader);
    }

    /**
     * Gets the username for Basic authentication.
     *
     * @return the username
     */
    public Option<String> getUsername() {
        return Option.of(username);
    }

    /**
     * Gets the password for Basic authentication.
     *
     * @return the password
     */
    public Option<String> getPassword() {
        return Option.of(password);
    }

}
