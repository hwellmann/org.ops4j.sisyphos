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
package org.ops4j.sisyphos.core.config;

import org.apache.tamaya.inject.api.Config;

/**
 * Global configuration of the Sisyphos application.
 *
 * @author Harald Wellmann
 *
 */
public interface Configuration {

    /**
     * Gets the data directory which contains input files for feeds.
     * @return data directory
     */
    @Config("sisyphos.directory.data")
    String getDataDirectory();

    /**
     * Gets the directory for logs and reports created by Sisyphos. Each simulation
     * run creates a subdirectory of this directory which will contain all simulation output.
     * @return
     */
    @Config("sisyphos.directory.reports")
    String getReportsDirectory();

    /**
     * Gets the directory which contains input templates for payloads. The templates may contain
     * placeholder to be interpolated with session attributes.
     * @return
     */
    @Config("sisyphos.directory.template")
    String getTemplateDirectory();

    /**
     * Gets the (list of) worker URIs for running distributed simulations.
     * @return comma-separated list of URIs
     */
    @Config("sisyphos.worker.uri")
    String getWorkerUri();
}