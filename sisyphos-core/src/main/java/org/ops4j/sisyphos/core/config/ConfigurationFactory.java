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
package org.ops4j.sisyphos.core.config;

import org.ops4j.spi.ServiceProviderFinder;

/**
 * Factory for system configurations.
 *
 * @author Harald Wellmann
 *
 */
public interface ConfigurationFactory {

    /**
     * Gets the system configuration.
     * @return configuration
     */
    Configuration getConfiguration();

    /**
     * Creates a configuration factory.
     * @return configuration factory
     */
    static ConfigurationFactory create() {
        return ServiceProviderFinder.loadUniqueServiceProvider(ConfigurationFactory.class);
    }

    /**
     * Creates a configuration factory and gets the configuration.
     * @return configuration
     */
    static Configuration configuration() {
        return create().getConfiguration();
    }
}
