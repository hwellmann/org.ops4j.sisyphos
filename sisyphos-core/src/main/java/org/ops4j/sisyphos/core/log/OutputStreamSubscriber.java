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

import java.io.OutputStream;
import java.io.PrintWriter;

import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Harald Wellmann
 *
 */
public class OutputStreamSubscriber implements Subscriber<StatisticsMessage> {

    private static Logger log = LoggerFactory.getLogger(OutputStreamSubscriber.class);

    private PrintWriter writer;


    private MessageToStringAdapter adapter;

    public OutputStreamSubscriber(OutputStream os) {
        this.writer = new PrintWriter(os, true);
        this.adapter = new MessageToStringAdapter();
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(StatisticsMessage t) {
        writer.println(adapter.adapt(t));
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
