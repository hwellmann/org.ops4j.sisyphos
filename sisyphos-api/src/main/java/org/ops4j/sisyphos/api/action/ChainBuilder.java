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
package org.ops4j.sisyphos.api.action;

import io.vavr.collection.List;

/**
 * Builds a chain of actions to be executed in the given order.
 *
 * @author Harald Wellmann
 *
 */
public class ChainBuilder implements Exec<ChainBuilder> {

    private List<ActionBuilder> actionBuilders;


    /**
     * Creates a chain builder from the given list of action builders.
     * @param actionBuilders action builders
     */
    public ChainBuilder(List<ActionBuilder> actionBuilders) {
        this.actionBuilders = actionBuilders;
    }

    @Override
    public List<ActionBuilder> actionBuilders() {
        return actionBuilders;
    }

    @Override
    public ChainBuilder newInstance(List<ActionBuilder> actionBuilders) {
        return new ChainBuilder(actionBuilders);
    }
}
