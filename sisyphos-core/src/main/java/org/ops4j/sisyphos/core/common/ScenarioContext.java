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
package org.ops4j.sisyphos.core.common;

import org.ops4j.sisyphos.api.action.ProtocolConfiguration;
import org.ops4j.sisyphos.core.message.StatisticsMessage;

import reactor.core.publisher.FluxSink;

/**
 * Execution context for a scenario.
 *
 * @author Harald Wellmann
 *
 */
public interface ScenarioContext {

    /**
     * Gets the sink for statistics messages generated during execution.
     *
     * @return message sink
     */
    FluxSink<StatisticsMessage> getMessageSink();

    /**
     * Gets the configuration for a given protocol.
     *
     * @param klass
     *            configuration class for a specific protocol (e.g. HTTP)
     * @return protocol configuration
     */
    <T extends ProtocolConfiguration> T getProtocolConfiguration(Class<T> klass);

}
