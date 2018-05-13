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
package org.ops4j.sisyphos.core.builder;

import org.ops4j.sisyphos.api.action.ActionBuilder;
import org.ops4j.sisyphos.core.common.Adapter;
import org.ops4j.spi.ServiceProviderFinder;

public class FluxBuilderAdapter implements Adapter<ActionBuilder, FluxBuilder> {

    @Override
    public FluxBuilder adapt(ActionBuilder actionBuilder) {
        for (FluxBuilderAdapterSpi adapter : ServiceProviderFinder.findServiceProviders(FluxBuilderAdapterSpi.class)) {
            FluxBuilder fluxBuilder = adapter.adapt(actionBuilder);
            if (fluxBuilder != null) {
                return fluxBuilder;
            }
        }

        throw new IllegalArgumentException(actionBuilder.getClass().getName());
    }
}
