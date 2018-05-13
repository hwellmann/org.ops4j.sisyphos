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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.el.ELManager;
import javax.el.ELProcessor;
import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.el.ValueExpression;

import org.assertj.core.util.Files;
import org.junit.Test;

import io.vavr.Function1;

/**
 * @author Harald Wellmann
 *
 */
public class ELProcessorTest {

    private AtomicInteger loadCount = new AtomicInteger();

    @Test
    public void shouldEvaluateCompositeExpression() {
        ELProcessor processor = new ELProcessor();
        processor.setVariable("t", "42");
        ExpressionFactory factory = ELManager.getExpressionFactory();
        StandardELContext elContext = processor.getELManager().getELContext();
        ValueExpression expr = factory.createValueExpression(elContext, "Number #{t}", String.class);
        Object value = expr.getValue(elContext);
        assertThat(value).isEqualTo("Number 42");
    }

    @Test
    public void shouldEvaluateTemplate() {
        ELProcessor processor = new ELProcessor();
        processor.setValue("authUser", "frodo");
        processor.setValue("authPassword", "precious");
        processor.setValue("authDomain", "hobbiton");
        processor.setValue("template", new HashMap<>());

        File templateFile = new File("src/test/resources/templates/login.json");
        String template = Files.contentOf(templateFile, StandardCharsets.UTF_8);
        processor.setValue("template['login']", template);

        ExpressionFactory factory = ELManager.getExpressionFactory();
        StandardELContext elContext = processor.getELManager().getELContext();
        ValueExpression expr = factory.createValueExpression(elContext, (String) processor.getValue("template['login']", String.class), String.class);
        String value = (String) expr.getValue(elContext);
        assertThat(value.contains("\"key\": \"precious\""));

        processor.setValue("loginKey", "changed");
        assertThat(value.contains("\"key\": \"changed\""));
    }

    @Test
    public void memoize() {
        Function1<String, String> f = Function1.of(this::loadTemplate).memoized();

        String path = "src/test/resources/templates/login.json";
        String content1 = f.apply(path);
        assertThat(loadCount.get()).isEqualTo(1);

        String content2 = f.apply(path);
        assertThat(loadCount.get()).isEqualTo(1);
        assertThat(content2).isEqualTo(content1);
    }

    private String loadTemplate(String path) {
        loadCount.incrementAndGet();
        return Files.contentOf(new File(path), StandardCharsets.UTF_8);
    }
}
