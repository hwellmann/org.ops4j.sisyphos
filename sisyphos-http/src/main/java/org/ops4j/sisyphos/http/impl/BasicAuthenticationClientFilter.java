/*
 * Copyright 2018 OPS4J Contributors
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
package org.ops4j.sisyphos.http.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

/**
 * Request filter adding a Basic authentication header.
 *
 * @author Harald Wellmann
 *
 */
public class BasicAuthenticationClientFilter implements ClientRequestFilter {

    private final String authHeader;

    /**
     * Creates a basic authentication filter with the given credentials.
     * @param username username
     * @param password password
     */
    public BasicAuthenticationClientFilter(String username, String password) {
        authHeader = createHeader(username, password);
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, authHeader);
    }

    private String createHeader(String username, String password) {
        StringBuffer buf = new StringBuffer(username);
        buf.append(':').append(password);
        return "Basic " + Base64.getEncoder().encodeToString(buf.toString().getBytes(StandardCharsets.UTF_8));
    }
}
