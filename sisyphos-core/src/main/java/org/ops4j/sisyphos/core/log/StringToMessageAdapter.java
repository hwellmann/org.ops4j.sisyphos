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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.api.session.Status;
import org.ops4j.sisyphos.core.builder.Adapter;
import org.ops4j.sisyphos.core.message.GroupMessage;
import org.ops4j.sisyphos.core.message.RequestMessage;
import org.ops4j.sisyphos.core.message.SessionEvent;
import org.ops4j.sisyphos.core.message.SessionMessage;
import org.ops4j.sisyphos.core.message.SimulationMessage;
import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.ops4j.sisyphos.core.session.ExtendedSession;
import org.ops4j.sisyphos.core.session.SessionImpl;

public class StringToMessageAdapter implements Adapter<String, StatisticsMessage> {

    @Override
    public StatisticsMessage adapt(String s) {
        Iterator<String> it = Arrays.asList(s.split("\t")).iterator();
        String type = it.next();
        switch (type) {
            case "REQUEST":
                return buildResponseMessage(it);
            case "USER":
                return buildSessionMessage(it);
            case "RUN":
                return buildSimulationMessage(it);
            case "GROUP":
                return buildGroupMessage(it);
        }
        throw new IllegalArgumentException(s);
    }

    private StatisticsMessage buildResponseMessage(Iterator<String> it) {
        String scenario = it.next();
        long userId = Long.parseLong(it.next());
        List<String> groups = Arrays.asList(it.next().split(","));
        String name = it.next();
        long requestTimestamp = Long.parseLong(it.next());
        long responseTimestamp = Long.parseLong(it.next());
        Status status = Status.valueOf(it.next());

        return new RequestMessage(scenario, userId, groups, name, requestTimestamp,
            responseTimestamp, status);
    }

    private StatisticsMessage buildSessionMessage(Iterator<String> it) {
        String scenario = it.next();
        long userId = Long.parseLong(it.next());
        Session session = new SessionImpl(scenario, userId);
        SessionEvent event = SessionEvent.valueOf(it.next());
        long startTimestamp = Long.parseLong(it.next());
        ((ExtendedSession) session).setStartDate(startTimestamp);
        long endTimestamp = Long.parseLong(it.next());
        return new SessionMessage(session, event, endTimestamp);
    }

    private StatisticsMessage buildSimulationMessage(Iterator<String> it) {
        String simulationName = it.next();
        String simulationId = it.next();
        long startTimestamp = Long.parseLong(it.next());
        String runDescription = it.next();
        return new SimulationMessage(simulationName, simulationId, startTimestamp, runDescription, "");
    }

    private StatisticsMessage buildGroupMessage(Iterator<String> it) {
        String scenario = it.next();
        long userId = Long.parseLong(it.next());
        List<String> groups = Arrays.asList(it.next().split(","));
        long startTimestamp = Long.parseLong(it.next());
        long endTimestamp = Long.parseLong(it.next());
        long cumulatedResponseTime = Long.parseLong(it.next());
        Status status = Status.valueOf(it.next());
        return new GroupMessage(scenario, userId, groups, startTimestamp, endTimestamp, cumulatedResponseTime, status);
    }
}
