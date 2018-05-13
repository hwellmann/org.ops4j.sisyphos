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

    public HttpConfiguration baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public HttpConfiguration acceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
        return this;
    }

    public HttpConfiguration acceptCharsetHeader(String acceptCharsetHeader) {
        this.acceptCharsetHeader = acceptCharsetHeader;
        return this;
    }

    public HttpConfiguration acceptLanguageHeader(String acceptLanguageHeader) {
        this.acceptLanguageHeader = acceptLanguageHeader;
        return this;
    }

    public HttpConfiguration acceptEncodingHeader(String acceptEncodingHeader) {
        this.acceptEncodingHeader = acceptEncodingHeader;
        return this;
    }

    public HttpConfiguration contentTypeHeader(String contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
        return this;
    }

    public HttpConfiguration userAgentHeader(String userAgentHeader) {
        this.userAgentHeader = userAgentHeader;
        return this;
    }

    public HttpConfiguration basicAuth(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * @return the baseUrl
     */
    public Option<String> getBaseUrl() {
        return Option.of(baseUrl);
    }

    /**
     * @return the acceptHeader
     */
    public Option<String> getAcceptHeader() {
        return Option.of(acceptHeader);
    }

    /**
     * @return the acceptCharsetHeader
     */
    public Option<String> getAcceptCharsetHeader() {
        return Option.of(acceptCharsetHeader);
    }

    /**
     * @return the acceptEncodingHeader
     */
    public Option<String> getAcceptEncodingHeader() {
        return Option.of(acceptEncodingHeader);
    }

    /**
     * @return the acceptLanguageHeader
     */
    public Option<String> getAcceptLanguageHeader() {
        return Option.of(acceptLanguageHeader);
    }

    /**
     * @return the contentTypeHeader
     */
    public Option<String> getContentTypeHeader() {
        return Option.of(contentTypeHeader);
    }

    /**
     * @return the userAgentHeader
     */
    public Option<String> getUserAgentHeader() {
        return Option.of(userAgentHeader);
    }

    /**
     * @return the username
     */
    public Option<String> getUsername() {
        return Option.of(username);
    }

    /**
     * @return the password
     */
    public Option<String> getPassword() {
        return Option.of(password);
    }


}
