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
package org.ops4j.sisyphos.core.message;

import org.ops4j.sisyphos.api.session.Session;

/**
 * @author Harald Wellmann
 *
 */
public class SessionMessage implements StatisticsMessage {

    private Session session;
    private SessionEvent sessionEvent;
    private long timestamp;


    public SessionMessage() {
    }

    public SessionMessage(Session session, SessionEvent sessionEvent, long timestamp) {
        this.session = session;
        this.sessionEvent = sessionEvent;
        this.timestamp = timestamp;
    }



    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public SessionEvent getSessionEvent() {
        return sessionEvent;
    }

    public void setSessionEvent(SessionEvent sessionEvent) {
        this.sessionEvent = sessionEvent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }



}
