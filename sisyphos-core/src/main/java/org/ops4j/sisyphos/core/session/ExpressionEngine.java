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
package org.ops4j.sisyphos.core.session;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.el.ELManager;
import javax.el.ELProcessor;
import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.el.ValueExpression;

import org.ops4j.sisyphos.api.session.Session;
import org.ops4j.sisyphos.core.config.ConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vavr.Function1;

/**
 * @author Harald Wellmann
 *
 */
public class ExpressionEngine {

    private static Logger log = LoggerFactory.getLogger(ExpressionEngine.class);

    private ELProcessor processor;
    private StandardELContext context;

    public ExpressionEngine(Session session) {
        this.processor = new ELProcessor();
        this.context = processor.getELManager().getELContext();
        ((SessionImpl) session).getAttributes().forEach((k, v) -> processor.setValue(k, v));
    }

    public <T> T eval(String expression, Class<T> type) {
        ExpressionFactory factory = ELManager.getExpressionFactory();
        ValueExpression expr = factory.createValueExpression(context, expression, type);
        return type.cast(expr.getValue(context));
    }

    public String eval(String expression) {
        return eval(expression, String.class);
    }

    public void setValue(String key, Object value) {
        processor.setValue(key, value);
    }

    public String getTemplate(String fileName) {
        return Function1.of(this::loadTemplate).memoized().apply(fileName);
    }

    public String evalTemplate(String fileName) {
        return eval(getTemplate(fileName), String.class);
    }

    private String loadTemplate(String fileName) {
        File templateDir = new File(ConfigurationFactory.configuration().getTemplateDirectory());
        File templateFile = new File(templateDir, fileName);
        try {
            return new String(Files.readAllBytes(templateFile.toPath()), StandardCharsets.UTF_8);
        }
        catch (IOException exc) {
            log.error("Cannot load template {}", fileName, exc);
            throw new IllegalArgumentException(exc);
        }
    }
}
