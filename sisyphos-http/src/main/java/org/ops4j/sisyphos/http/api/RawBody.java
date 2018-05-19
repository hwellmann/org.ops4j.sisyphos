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

import javax.ws.rs.client.Entity;

import org.ops4j.sisyphos.api.session.Session;

/**
 * Represents a body entity as a literal string.
 *
 * @author Harald Wellmann
 *
 */
public class RawBody implements HttpBody<String> {

    private String rawContent;

    RawBody(String rawContent) {
        this.rawContent = rawContent;
    }

    @Override
    public Entity<String> toEntity(Session session, String contentType) {
        return Entity.entity(rawContent, contentType);
    }

}
