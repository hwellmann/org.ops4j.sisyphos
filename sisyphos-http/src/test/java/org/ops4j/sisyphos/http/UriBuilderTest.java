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
package org.ops4j.sisyphos.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.junit.Test;

/**
 * @author Harald Wellmann
 *
 */
public class UriBuilderTest {

    @Test
    public void shouldResolveFromEmptyPath() {
        assertThat(UriBuilder.fromPath("").build().resolve("http://localhost:1234/foo?bar=1"))
            .isEqualTo(URI.create("http://localhost:1234/foo?bar=1"));
    }

    @Test
    public void shouldResolveFromSlashTerminatedPath() {
        URI uri1 = URI.create("http://localhost:8081/myapp/");
        URI uri2 = UriBuilder.fromPath("authentication/credentials").queryParam("user", "tom").queryParam("password", "secret").build();
        assertThat(uri1.resolve(uri2)).isEqualTo(URI.create("http://localhost:8081/myapp/authentication/credentials?user=tom&password=secret"));

    }

    @Test
    public void shouldResolveFromUnterminatedPath() {
        URI uri1 = URI.create("http://localhost:8081/myapp");
        URI uri2 = UriBuilder.fromPath("authentication/credentials").queryParam("user", "tom").queryParam("password", "secret").build();
        assertThat(uri1.resolve(uri2)).isEqualTo(URI.create("http://localhost:8081/authentication/credentials?user=tom&password=secret"));

    }
}
