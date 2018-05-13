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
package org.ops4j.sisyphos.core.tamaya;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.apache.tamaya.Configuration;
import org.apache.tamaya.ConfigurationProvider;
import org.apache.tamaya.inject.ConfigurationInjection;
import org.apache.tamaya.inject.api.Config;
import org.apache.tamaya.spi.ConfigurationContext;
import org.apache.tamaya.spisupport.PropertySourceComparator;
import org.apache.tamaya.spisupport.SimplePropertySource;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TamayaInjectionTest {

    @Before
    public void before() {
        System.setProperty("sisyphos.directory.data", "src/test/resources/data");
    }

    @After
    public void after() {
        System.clearProperty("sisyphos.directory.data");
    }

    public static interface SisyphosConfiguration {

        @Config("sisyphos.directory.data")
        String getDataDirectory();
    }

    public static class ConfigurationImpl implements org.ops4j.sisyphos.core.config.Configuration {

        @Config("sisyphos.directory.data")
        private String dataPath;

        @Config("sisyphos.directory.reports")
        private String reportPath;

        @Config("sisyphos.directory.template")
        private String templatePath;

        @Override
        public String getDataDirectory() {
            return dataPath;
        }

        @Override
        public String getReportsDirectory() {
            return reportPath;
        }

        @Override
        public String getTemplateDirectory() {
            return templatePath;
        }

        @Override
        public String getWorkerUri() {
            return "http://localhost:8080";
        }
    }

    @Test
    public void shouldCreateTemplateWithDefaultConfiguration() {

        SisyphosConfiguration config = ConfigurationInjection.getConfigurationInjector().createTemplate(SisyphosConfiguration.class);
        assertThat(config).isNotNull();
        assertThat(config.getDataDirectory()).isEqualTo("src/test/resources/data");
    }

    @Test
    @Ignore("TAMAYA-336")
    public void shouldCreateTemplateWithCustomConfiguration() {
        SimplePropertySource propertySource = new SimplePropertySource(new File("src/test/resources/test.properties"));
        propertySource.setOrdinal(1100);
        ConfigurationContext context = ConfigurationProvider.getConfigurationContextBuilder().addDefaultPropertySources()
            .addPropertySources(propertySource)
            .sortPropertySources(PropertySourceComparator.getInstance())
            .build();
        Configuration configuration = ConfigurationProvider.createConfiguration(context);

        SisyphosConfiguration config = ConfigurationInjection.getConfigurationInjector()
            .createTemplate(SisyphosConfiguration.class, configuration);

        assertThat(configuration.get("sisyphos.directory.data")).isEqualTo("testdata");
        assertThat(config).isNotNull();
        assertThat(config.getDataDirectory()).isEqualTo("testdata");
    }

    @Test
    public void shouldInjectCustomConfiguration() {
        SimplePropertySource propertySource = new SimplePropertySource(new File("src/test/resources/test.properties"));
        propertySource.setOrdinal(1100);
        ConfigurationContext context = ConfigurationProvider.getConfigurationContextBuilder().addDefaultPropertySources()
            .addPropertySources(propertySource)
            .sortPropertySources(PropertySourceComparator.getInstance())
            .build();
        Configuration configuration = ConfigurationProvider.createConfiguration(context);



        org.ops4j.sisyphos.core.config.Configuration config = ConfigurationInjection.getConfigurationInjector()
            .configure(new ConfigurationImpl(), configuration);

        assertThat(configuration.get("sisyphos.directory.data")).isEqualTo("testdata");
        assertThat(config).isNotNull();
        assertThat(config.getDataDirectory()).isEqualTo("testdata");
    }

}
