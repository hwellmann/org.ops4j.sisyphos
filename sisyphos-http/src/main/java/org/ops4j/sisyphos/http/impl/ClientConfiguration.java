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
package org.ops4j.sisyphos.http.impl;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.ops4j.sisyphos.http.api.HttpConfiguration;

/**
 * @author Harald Wellmann
 *
 */
public final class ClientConfiguration {

    private ClientConfiguration() {
        // hidden constructor
    }

    public static Client client(HttpConfiguration httpConfig) {

        Client client = ClientBuilder.newClient()
                .register(LoggingClientFilter.class);

        if (httpConfig.getUsername().isDefined()) {
            BasicAuthenticationClientFilter authFilter = new BasicAuthenticationClientFilter(httpConfig.getUsername().get(), httpConfig.getPassword().get());
            client.register(authFilter);
        }

        return client;
    }
}