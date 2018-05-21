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
package org.ops4j.sisyphos.core.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.ops4j.sisyphos.core.config.ConfigurationFactory;
import org.ops4j.sisyphos.core.message.RunMessage;
import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Harald Wellmann
 *
 */
public class LogSubscriber implements Subscriber<StatisticsMessage> {

    private static Logger log = LoggerFactory.getLogger(LogSubscriber.class);

    private PrintWriter writer;

    private MessageToStringAdapter adapter;

    private String subdirectory;


    public LogSubscriber(String subdirectory) {
        this.subdirectory = subdirectory;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        adapter = new MessageToStringAdapter();
    }

    @Override
    public void onNext(StatisticsMessage t) {
        if (t instanceof RunMessage) {
            openWriter((RunMessage) t);
        }
        writer.println(adapter.adapt(t));
    }

    private void openWriter(RunMessage msg) {
        String reportPath = ConfigurationFactory.configuration().getReportsDirectory();
        File reportDir = new File(reportPath, getSubdirectoryOrFallback(msg));
        if (!reportDir.exists() && !reportDir.mkdirs()) {
            log.error("Cannot create report directory {}", reportDir);
            return;
        }
        File reportFile = new File(reportDir, "simulation.log");
        try {
            writer = new PrintWriter(reportFile, StandardCharsets.UTF_8.name());
        }
        catch (IOException exc) {
            log.error("Cannot open log file", exc);
        }
    }

    private String getSubdirectoryOrFallback(RunMessage msg) {

        if (subdirectory == null) {
            String simulationId = msg.getUserDefinedSimulationId();
            if (simulationId == null || simulationId.isEmpty()) {
                simulationId = msg.getDefaultSimulationId();
            }
            subdirectory = String.format("%s-%d", simulationId, msg.getStartTime());
        }
        return subdirectory;
    }

    @Override
    public void onError(Throwable t) {
        log.error("Error on log stream", t);
    }

    @Override
    public void onComplete() {
        writer.close();
    }
}
