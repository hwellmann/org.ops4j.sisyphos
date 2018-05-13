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
import org.apache.tamaya.spi.ConfigurationContext;
import org.apache.tamaya.spisupport.PropertySourceComparator;
import org.apache.tamaya.spisupport.SimplePropertySource;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TamayaCoreTest {

    @Before
    public void before() {
        System.setProperty("sisyphos.directory.data", "src/test/resources/data");
    }

    @After
    public void after() {
        System.clearProperty("sisyphos.directory.data");
    }

    @Test
    public void shouldGetValueViaSystemProperty() {
        Configuration config = ConfigurationProvider.getConfiguration();
        assertThat(config.get("sisyphos.directory.data")).isEqualTo("src/test/resources/data");
    }

    @Test
    public void shouldGetValueViaCustomPropertySource() {
        SimplePropertySource propertySource = new SimplePropertySource(new File("src/test/resources/test.properties"));
        propertySource.setOrdinal(1100);
        ConfigurationContext context = ConfigurationProvider.getConfigurationContextBuilder().addDefaultPropertySources()//ConfigurationProvider.getConfiguration().getContext().toBuilder()
            .addPropertySources(propertySource)
            .sortPropertySources(PropertySourceComparator.getInstance())
            .build();
        Configuration config = ConfigurationProvider.createConfiguration(context);
        assertThat(config.get("sisyphos.directory.data")).isEqualTo("testdata");
    }

    @Test
    @Ignore("TAMAYA-335")
    public void shouldGetValueViaCustomPropertySourceFromBuilder() {
        SimplePropertySource propertySource = SimplePropertySource.newBuilder()
                .withProperties(new File("src/test/resources/test.properties"))
                .withName("test.properties")
                .withOrdinal(1100).build();
        ConfigurationContext context = ConfigurationProvider.getConfigurationContextBuilder().addDefaultPropertySources()
            .addPropertySources(propertySource)
            .sortPropertySources(PropertySourceComparator.getInstance())
            .build();
        Configuration config = ConfigurationProvider.createConfiguration(context);
        assertThat(config.get("sisyphos.directory.data")).isEqualTo("testdata");
    }

}
