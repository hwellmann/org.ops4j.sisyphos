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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.tamaya.ConfigException;
import org.apache.tamaya.core.propertysource.BasePropertySource;
import org.apache.tamaya.spi.PropertyValue;

public class SisyphosPropertySource extends BasePropertySource {

    @Override
    public Map<String, PropertyValue> getProperties() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("sisyphos.properties");
        if (is == null) {
            return Collections.emptyMap();
        }
        Map<String, PropertyValue> properties = new HashMap<>();
        Properties props = new Properties();
        try {
            props.load(is);
            for (String key : props.stringPropertyNames()) {
                properties.put(key, PropertyValue.of(key, props.getProperty(key), "sisyphos.properties"));
            }
        }
        catch (IOException e) {
            throw new ConfigException("Error loading properties from sisyphos.properties");
        }
        return properties;
    }

    @Override
    public int getOrdinal() {
        return 90;
    }

}
