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
package org.ops4j.sisyphos.core.runner;

import org.ops4j.sisyphos.api.action.ProtocolConfiguration;
import org.ops4j.sisyphos.core.builder.ScenarioContext;
import org.ops4j.sisyphos.core.config.ConfigurationFactory;
import org.ops4j.sisyphos.core.message.StatisticsMessage;

import reactor.core.publisher.FluxSink;

/**
 * @author Harald Wellmann
 *
 */
public class DefaultScenarioContext implements ScenarioContext {


    private FluxSink<StatisticsMessage> messageSink;
    private ProtocolConfiguration protocolConfiguration;

    public DefaultScenarioContext(FluxSink<StatisticsMessage> messages, ProtocolConfiguration protocolConfiguration) {
        this.messageSink = messages;
        this.protocolConfiguration = protocolConfiguration;
    }

    @Override
    public FluxSink<StatisticsMessage> messageSink() {
        return messageSink;
    }

    @Override
    public String getDataPath() {
        return ConfigurationFactory.configuration().getDataDirectory();
    }

    @Override
    public <T extends ProtocolConfiguration> T getProtocolConfiguration(Class<T> klass) {
        return klass.cast(protocolConfiguration);
    }
}
