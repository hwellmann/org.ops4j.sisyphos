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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A combined filter and interceptor which logs HTTP requests and responses.
 * <p>
 * The log output includes request method and URI, request and respose headers, and request and
 * response bodies, truncated after 8 KB.
 *
 * @author Harald Wellmann
 *
 */
public class LoggingClientFilter
    implements ClientRequestFilter, ClientResponseFilter, WriterInterceptor {

    private static final String ENTITY_STREAM_PROPERTY = "LoggingClientFilter.entityStream";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int MAX_ENTITY_SIZE = 8 * 1024;

    private static Logger log = LoggerFactory.getLogger(LoggingClientFilter.class);

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        log.debug(">> Request = {} {}", requestContext.getMethod(), requestContext.getUri());
        for (String header : requestContext.getHeaders().keySet()) {
            log.debug(">> {} = {}", header, requestContext.getHeaderString(header));
        }
        if (requestContext.hasEntity()) {
            OutputStream stream = new CopyingStream(requestContext.getEntityStream());
            requestContext.setEntityStream(stream);
            requestContext.setProperty(ENTITY_STREAM_PROPERTY, stream);
        }
    }

    private void log(StringBuilder sb) {
        log.debug(sb.toString());
    }

    private InputStream logInboundEntity(StringBuilder b, InputStream is,
        final Charset charset) throws IOException {
        InputStream stream = is.markSupported() ? is : new BufferedInputStream(is);
        stream.mark(MAX_ENTITY_SIZE);
        byte[] entity = new byte[MAX_ENTITY_SIZE];
        int entitySize = stream.read(entity);
        b.append(new String(entity, 0, Math.min(entitySize, MAX_ENTITY_SIZE), charset));
        if (entitySize > MAX_ENTITY_SIZE) {
            b.append("...more...");
        }
        b.append('\n');
        stream.reset();
        return stream;
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext)
        throws IOException {
        log.debug("<< Response");
        for (String header : responseContext.getHeaders().keySet()) {
            log.debug("<< {} = {}", header, responseContext.getHeaderString(header));
        }
        StringBuilder sb = new StringBuilder("");
        if (responseContext.hasEntity()) {
            responseContext.setEntityStream(
                logInboundEntity(sb, responseContext.getEntityStream(), DEFAULT_CHARSET));
            log(sb);
        }

    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
        throws IOException, WebApplicationException {
        CopyingStream stream = (CopyingStream) context.getProperty(ENTITY_STREAM_PROPERTY);
        context.proceed();
        if (stream != null) {
            log(stream.getStringBuilder(DEFAULT_CHARSET));
        }
    }

    /**
     * Wraps an output stream, copying all written bytes up to a given maximum size to an internal
     * buffer.
     *
     * @author Harald Wellmann
     *
     */
    private static class CopyingStream extends FilterOutputStream {

        private StringBuilder sb = new StringBuilder();
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        /**
         * Creates a copying stream wrapping the given stream.
         *
         * @param out
         *            stream to be wrapped
         */
        CopyingStream(OutputStream out) {
            super(out);
        }

        /**
         * Gets a string builder using the given character set to convert the buffered bytes to a
         * string.
         *
         * @param charset
         *            character set for buffered bytes
         * @return string builder
         */
        StringBuilder getStringBuilder(Charset charset) {
            byte[] entity = baos.toByteArray();

            sb.append(new String(entity, 0, entity.length, charset));
            if (entity.length > MAX_ENTITY_SIZE) {
                sb.append("...more...");
            }
            sb.append('\n');

            return sb;
        }

        @Override
        public void write(int i) throws IOException {
            if (baos.size() < MAX_ENTITY_SIZE) {
                baos.write(i);
            }
            out.write(i);
        }
    }
}
