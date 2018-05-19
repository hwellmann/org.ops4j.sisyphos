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

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.ops4j.sisyphos.api.action.RequestActionBuilder;
import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.simulation.Sisyphos;
import org.ops4j.sisyphos.core.builder.FluxBuilder;
import org.ops4j.sisyphos.core.builder.FluxBuilderAdapter;
import org.ops4j.sisyphos.core.common.ScenarioContext;
import org.ops4j.sisyphos.core.session.ExpressionEngine;
import org.ops4j.sisyphos.core.session.SessionImpl;
import org.ops4j.sisyphos.http.api.Check;
import org.ops4j.sisyphos.http.api.CheckBuilder;
import org.ops4j.sisyphos.http.api.HttpBody;
import org.ops4j.sisyphos.http.api.HttpConfiguration;
import org.ops4j.sisyphos.http.api.HttpRequestBuilder;

import io.vavr.collection.List;
import reactor.core.publisher.Flux;

/**
 * Builds a flux representing an HTTP request to be executed on a session.
 *
 * @author Harald Wellmann
 *
 */
public class HttpRequestFluxBuilder implements FluxBuilder {


    private HttpRequestBuilder httpRequestBuilder;
    private List<Check<Response, ?>> checks;

    public HttpRequestFluxBuilder(HttpRequestBuilder httpRequestBuilder) {
        this.httpRequestBuilder = httpRequestBuilder;
    }

    @Override
    public Flux<Session> buildFlux(ScenarioContext context) {
        HttpConfiguration httpConfig = context.getProtocolConfiguration(HttpConfiguration.class);
        checks = httpRequestBuilder.getCheckBuilders().map(CheckBuilder::build);
        RequestActionBuilder requestActionBuilder = Sisyphos.request(httpRequestBuilder.getRequestName(), session -> execute(httpConfig, session));
        FluxBuilderAdapter adapter = new FluxBuilderAdapter();
        return adapter.adapt(requestActionBuilder).buildFlux(context);
    }

    public Session execute(HttpConfiguration httpConfig, Session session) {
        Client client = HttpClientBuilder.client(httpConfig);
        Builder request = client.target(buildUri(httpConfig, session)).request(accept(httpConfig));
        httpConfig.getAcceptCharsetHeader().map(h -> addHeader(request, session, HttpHeaders.ACCEPT_CHARSET, h));
        httpConfig.getAcceptEncodingHeader().map(h -> addHeader(request, session, HttpHeaders.ACCEPT_ENCODING, h));
        httpConfig.getAcceptLanguageHeader().map(h -> addHeader(request, session, HttpHeaders.ACCEPT_LANGUAGE, h));
        httpConfig.getUserAgentHeader().map(h -> addHeader(request, session, HttpHeaders.USER_AGENT, h));
        addHeaders(request, session);

        Response response = request.build(httpRequestBuilder.getMethodName(), bodyEntity(httpConfig, session)).invoke();
        checks.forEach(c -> c.check(response, session));
        client.close();
        return session;
    }

    private URI buildUri(HttpConfiguration httpConfig, Session session) {
        ExpressionEngine engine = ((SessionImpl) session).getEngine();
        String relative = engine.eval(httpRequestBuilder.getUrl());
        URI baseUri = UriBuilder.fromUri(httpConfig.getBaseUrl().getOrElse("")).build();
        return baseUri.resolve(relative);
    }

    private String[] accept(HttpConfiguration httpConfig) {
        return httpConfig.getAcceptHeader().map(h -> new String[] { h }).get();
    }

    private Builder addHeaders(Builder request, Session session) {
        MultivaluedMap<String, Object> headers = httpRequestBuilder.getHeaders();
        for (String key : headers.keySet()) {
            for (Object value : headers.get(key)) {
                addHeader(request, session, key, value);
            }
        }
        return request;
    }

    private String addHeader(Builder request, Session session, String key, Object value) {
        ExpressionEngine engine = ((SessionImpl) session).getEngine();
        String header = engine.eval(value.toString());
        request.header(key, header);
        return header;
    }

    private boolean hasFormParams() {
        return !httpRequestBuilder.getForm().asMap().isEmpty();
    }

    private Entity<?> bodyEntity(HttpConfiguration httpConfig, Session session) {
        HttpBody<String> body = httpRequestBuilder.getBody();
        if (body == null && !hasFormParams()) {
            return null;
        }
        if (body != null) {
            String contentType = httpConfig.getContentTypeHeader().getOrElse(MediaType.APPLICATION_OCTET_STREAM);
            return body.toEntity(session, contentType);
        }
        return Entity.form(httpRequestBuilder.getForm());
    }
}
