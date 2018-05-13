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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonObject;

import org.assertj.core.util.Files;
import org.junit.Test;
import org.ops4j.sisyphos.core.session.ExpressionEngine;
import org.ops4j.sisyphos.core.session.SessionImpl;

/**
 * @author Harald Wellmann
 *
 */
public class ExpressionEngineTest {

    @Test
    public void shouldEvaluate() {
        SessionImpl session = new SessionImpl(1);
        ExpressionEngine engine = session.getEngine();

        session.setAttribute("authUser", "frodo");
        session.setAttribute("authPassword", "precious");
        session.setAttribute("authDomain", "hobbiton");

        File templateFile = new File("src/test/resources/templates/login.json");
        String template = Files.contentOf(templateFile, StandardCharsets.UTF_8);

        JsonObject json = templateToJson(engine, template);
        assertThat(json.getString("login")).isEqualTo("frodo");

        session.setAttribute("authUser", "merry");
        json = templateToJson(engine, template);
        assertThat(json.getString("login")).isEqualTo("merry");

    }

    /**
     * @param engine
     * @param template
     * @return
     */
    private JsonObject templateToJson(ExpressionEngine engine, String template) {
        String result = engine.eval(template, String.class);
        return Json.createReader(new StringReader(result)).readObject();
    }

}
