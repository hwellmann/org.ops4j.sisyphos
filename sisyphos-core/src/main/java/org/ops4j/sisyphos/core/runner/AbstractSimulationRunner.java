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
package org.ops4j.sisyphos.core.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ops4j.sisyphos.api.simulation.SimulationRunner;
import org.ops4j.sisyphos.core.message.StatisticsMessage;
import org.reactivestreams.Subscriber;

/**
 * @author Harald Wellmann
 *
 */
public abstract class AbstractSimulationRunner implements SimulationRunner {

    private List<Subscriber<StatisticsMessage>> subscribers = new ArrayList<>();

    public void addMessageSubscriber(Subscriber<StatisticsMessage> subscriber) {
        subscribers.add(subscriber);
    }

    public List<Subscriber<StatisticsMessage>> getMessageSubscribers() {
        return Collections.unmodifiableList(subscribers);
    }
}
