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
package org.ops4j.sisyphos.core.log;

import static java.util.stream.Collectors.joining;

import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.core.builder.Adapter;
import org.ops4j.sisyphos.core.message.GroupMessage;
import org.ops4j.sisyphos.core.message.RequestMessage;
import org.ops4j.sisyphos.core.message.SessionMessage;
import org.ops4j.sisyphos.core.message.SimulationMessage;
import org.ops4j.sisyphos.core.message.StatisticsMessage;

public class MessageToStringAdapter implements Adapter<StatisticsMessage, String> {

    @Override
    public String adapt(StatisticsMessage t) {
        if (t instanceof SessionMessage) {
            return onSessionMessage((SessionMessage) t);
        }
        else if (t instanceof SimulationMessage) {
            return onSimulationMessage((SimulationMessage) t);
        }
        else if (t instanceof RequestMessage) {
            return onResponseMessage((RequestMessage) t);
        }
        else if (t instanceof RequestMessage) {
            return onResponseMessage((RequestMessage) t);
        }
        else if (t instanceof GroupMessage) {
            return onGroupMessage((GroupMessage) t);
        }

        throw new IllegalArgumentException(t.toString());
    }

    private String onResponseMessage(RequestMessage msg) {
        String group = msg.getGroups().stream().collect(joining(","));
        String message = msg.getMessage();
        return String.format("REQUEST\t%s\t%d\t%s\t%s\t%d\t%d\t%s\t%s",
            msg.getScenario(), msg.getUserId(), group, msg.getName(), msg.getRequestTimestamp(), msg.getResponseTimestamp(), msg.getStatus(), message);
    }

    private String onSessionMessage(SessionMessage msg) {
        Session session = msg.getSession();
        return String.format("USER\t%s\t%d\t%s\t%d\t%d",
            session.getScenario(), session.getUserId(), msg.getSessionEvent(), session.getStartDate(), msg.getTimestamp());
    }

    private String onSimulationMessage(SimulationMessage msg) {

        String userDefinedId = "";
        return String.format("RUN\t%s\t%s\t%s\t%d\t%s\t2.0",
            msg.getSimulationName(), userDefinedId, msg.getSimulationId(), msg.getStartTime(), emptyIfNull(msg.getRunDescription()));
    }

    private String onGroupMessage(GroupMessage msg) {
        String group = msg.getGroups().stream().collect(joining(","));
        return String.format("GROUP\t%s\t%d\t%s\t%d\t%d\t%d\t%s",
            msg.getScenario(), msg.getUserId(), group, msg.getStartTimestamp(), msg.getEndTimestamp(), msg.getCumulatedResponseTime(), msg.getStatus());
    }

    private String emptyIfNull(String text) {
        return (text == null)? "" : text;
    }
}
