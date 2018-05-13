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
import org.ops4j.sisyphos.api.action.ChainBuilder;
import org.ops4j.sisyphos.api.action.ConsumeActionBuilder;
import org.ops4j.sisyphos.api.action.DuringBuilder;
import org.ops4j.sisyphos.api.action.EmptyActionBuilder;
import org.ops4j.sisyphos.api.action.GroupEndActionBuilder;
import org.ops4j.sisyphos.api.action.GroupStartActionBuilder;
import org.ops4j.sisyphos.api.action.IfBuilder;
import org.ops4j.sisyphos.api.action.PauseBuilder;
import org.ops4j.sisyphos.api.action.RandomSwitchBuilder;
import org.ops4j.sisyphos.api.action.RepeatBuilder;
import org.ops4j.sisyphos.api.action.RequestActionBuilder;
import org.ops4j.sisyphos.api.action.RetryBuilder;
import org.ops4j.sisyphos.api.simulation.ScenarioBuilder;

public class CoreFluxBuilderAdapter implements FluxBuilderAdapterSpi {

    @Override
    public FluxBuilder adapt(ActionBuilder actionBuilder) {
        if (actionBuilder instanceof ScenarioBuilder) {
            return adapt((ScenarioBuilder) actionBuilder);
        }
        if (actionBuilder instanceof ChainBuilder) {
            return adapt((ChainBuilder) actionBuilder);
        }

        if (actionBuilder instanceof ConsumeActionBuilder) {
            return adapt((ConsumeActionBuilder<?>) actionBuilder);
        }

        if (actionBuilder instanceof RequestActionBuilder) {
            return adapt((RequestActionBuilder) actionBuilder);
        }

        if (actionBuilder instanceof PauseBuilder) {
            return adapt((PauseBuilder) actionBuilder);
        }

        if (actionBuilder instanceof DuringBuilder) {
            return adapt((DuringBuilder) actionBuilder);
        }

        if (actionBuilder instanceof RepeatBuilder) {
            return adapt((RepeatBuilder) actionBuilder);
        }

        if (actionBuilder instanceof RetryBuilder) {
            return adapt((RetryBuilder) actionBuilder);
        }

        if (actionBuilder instanceof GroupStartActionBuilder) {
            return adapt((GroupStartActionBuilder) actionBuilder);
        }

        if (actionBuilder instanceof GroupEndActionBuilder) {
            return adapt((GroupEndActionBuilder) actionBuilder);
        }

        if (actionBuilder instanceof IfBuilder) {
            return adapt((IfBuilder) actionBuilder);
        }

        if (actionBuilder instanceof EmptyActionBuilder) {
            return adapt((EmptyActionBuilder) actionBuilder);
        }

        if (actionBuilder instanceof RandomSwitchBuilder) {
            return adapt((RandomSwitchBuilder) actionBuilder);
        }
        return null;
    }

    public FluxBuilder adapt(ScenarioBuilder scenarioBuilder) {
        return new ScenarioFluxBuilder(scenarioBuilder);
    }

    public FluxBuilder adapt(ChainBuilder chainBuilder) {
        return new ChainFluxBuilder(chainBuilder);
    }

    public FluxBuilder adapt(RequestActionBuilder requestActionBuilder) {
        return new RequestActionFluxBuilder(requestActionBuilder);
    }

    public <F> FluxBuilder adapt(ConsumeActionBuilder<F> consumeActionBuilder) {
        return new ConsumeActionFluxBuilder<F>(consumeActionBuilder);
    }

    public FluxBuilder adapt(PauseBuilder pauseBuilder) {
        return new PauseFluxBuilder(pauseBuilder);
    }

    public FluxBuilder adapt(RepeatBuilder repeatBuilder) {
        return new RepeatFluxBuilder(repeatBuilder);
    }

    public FluxBuilder adapt(RetryBuilder repeatBuilder) {
        return new RetryFluxBuilder(repeatBuilder);
    }

    public FluxBuilder adapt(DuringBuilder duringBuilder) {
        return new DuringFluxBuilder(duringBuilder);
    }

    public FluxBuilder adapt(GroupStartActionBuilder actionBuilder) {
        return new GroupStartFluxBuilder(actionBuilder);
    }

    public FluxBuilder adapt(GroupEndActionBuilder actionBuilder) {
        return new GroupEndFluxBuilder(actionBuilder);
    }

    public FluxBuilder adapt(IfBuilder actionBuilder) {
        return new IfFluxBuilder(actionBuilder);
    }

    public FluxBuilder adapt(EmptyActionBuilder actionBuilder) {
        return new EmptyFluxBuilder();
    }

    public FluxBuilder adapt(RandomSwitchBuilder actionBuilder) {
        return new RandomSwitchFluxBuilder(actionBuilder);
    }
}
