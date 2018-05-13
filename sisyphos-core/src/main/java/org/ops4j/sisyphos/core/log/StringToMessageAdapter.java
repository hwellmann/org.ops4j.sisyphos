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
import org.ops4j.sisyphos.core.session.SessionImpl;

public class StringToMessageAdapter implements Adapter<String, StatisticsMessage> {

    @Override
    public StatisticsMessage adapt(String s) {
        String[] parts = s.split("\t");
        String type = parts[0];
        switch (type) {
            case "REQUEST":
                return buildResponseMessage(parts);
            case "USER":
                return buildSessionMessage(parts);
            case "RUN":
                return buildSimulationMessage(parts);
            case "GROUP":
                return buildGroupMessage(parts);
        }
        throw new IllegalArgumentException(s);
    }

    private StatisticsMessage buildResponseMessage(String[] parts) {
        String scenario = parts[1];
        long userId = Long.parseLong(parts[2]);
        List<String> groups = Arrays.asList(parts[3].split(","));
        String name = parts[4];
        long requestTimestamp = Long.parseLong(parts[5]);
        long responseTimestamp = Long.parseLong(parts[6]);
        Status status = Status.valueOf(parts[7]);

        return new RequestMessage(scenario, userId, groups, name, requestTimestamp,
            responseTimestamp, status);
    }

    private StatisticsMessage buildSessionMessage(String[] parts) {
        String scenario = parts[1];
        long userId = Long.parseLong(parts[2]);
        Session session = new SessionImpl(scenario, userId);
        SessionEvent event = SessionEvent.valueOf(parts[3]);
        long startTimestamp = Long.parseLong(parts[4]);
        session.setStartDate(startTimestamp);
        long endTimestamp = Long.parseLong(parts[5]);
        return new SessionMessage(session, event, endTimestamp);
    }

    private StatisticsMessage buildSimulationMessage(String[] parts) {
        String simulationName = parts[1];
        String simulationId = parts[2];
        long startTimestamp = Long.parseLong(parts[3]);
        String runDescription = parts[4];
        return new SimulationMessage(simulationName, simulationId, startTimestamp, runDescription, "");
    }

    private StatisticsMessage buildGroupMessage(String[] parts) {
        String scenario = parts[1];
        long userId = Long.parseLong(parts[2]);
        List<String> groups = Arrays.asList(parts[3].split(","));
        long startTimestamp = Long.parseLong(parts[4]);
        long endTimestamp = Long.parseLong(parts[5]);
        long cumulatedResponseTime = Long.parseLong(parts[6]);
        Status status = Status.valueOf(parts[7]);
        return new GroupMessage(scenario, userId, groups, startTimestamp, endTimestamp, cumulatedResponseTime, status);
    }
}
