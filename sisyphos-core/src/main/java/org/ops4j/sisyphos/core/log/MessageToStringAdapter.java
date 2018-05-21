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
import org.ops4j.sisyphos.core.common.Adapter;
import org.ops4j.sisyphos.core.message.GroupMessage;
import org.ops4j.sisyphos.core.message.RequestMessage;
import org.ops4j.sisyphos.core.message.RunMessage;
import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.ops4j.sisyphos.core.message.UserMessage;

public class MessageToStringAdapter implements Adapter<StatisticsMessage, String> {

    @Override
    public String adapt(StatisticsMessage t) {
        if (t instanceof UserMessage) {
            return onUserMessage((UserMessage) t);
        }
        else if (t instanceof RunMessage) {
            return onRunMessage((RunMessage) t);
        }
        else if (t instanceof RequestMessage) {
            return onRequestMessage((RequestMessage) t);
        }
        else if (t instanceof GroupMessage) {
            return onGroupMessage((GroupMessage) t);
        }

        throw new IllegalArgumentException(t.toString());
    }

    private String onRequestMessage(RequestMessage msg) {
        String group = msg.getGroups().stream().collect(joining(","));
        String message = msg.getMessage();
        return String.format("REQUEST\t%s\t%d\t%s\t%s\t%d\t%d\t%s\t%s",
            msg.getScenario(), msg.getUserId(), group, msg.getName(), msg.getRequestTimestamp(), msg.getResponseTimestamp(), msg.getStatus(), emptyIfNull(message));
    }

    private String onUserMessage(UserMessage msg) {
        Session session = msg.getSession();
        return String.format("USER\t%s\t%d\t%s\t%d\t%d",
            session.getScenario(), session.getUserId(), msg.getSessionEvent(), session.getStartDate(), msg.getTimestamp());
    }

    private String onRunMessage(RunMessage msg) {

        return String.format("RUN\t%s\t%s\t%s\t%d\t%s\t%s",
            msg.getSimulationName(), msg.getUserDefinedSimulationId(), msg.getDefaultSimulationId(), msg.getStartTime(), emptyIfNull(msg.getRunDescription()), msg.getVersion());
    }

    private String onGroupMessage(GroupMessage msg) {
        String group = msg.getGroups().stream().collect(joining(","));
        return String.format("GROUP\t%s\t%d\t%s\t%d\t%d\t%d\t%s",
            msg.getScenario(), msg.getUserId(), group, msg.getStartTimestamp(), msg.getEndTimestamp(), msg.getCumulatedResponseTime(), msg.getStatus());
    }

    private String emptyIfNull(String text) {
        return (text == null) ? "" : text;
    }
}
