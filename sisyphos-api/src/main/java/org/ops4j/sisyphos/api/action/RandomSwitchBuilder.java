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

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

/**
 * @author Harald Wellmann
 *
 */
public class RandomSwitchBuilder implements ActionBuilder {

    private List<Tuple2<Double, ActionBuilder>> actionBuilders;
    private double totalWeight;


    RandomSwitchBuilder(List<ActionBuilder> actionBuilders) {
        this(actionBuilders.toMap(a -> Tuple.of(1.0, a)));
    }

    RandomSwitchBuilder(Map<Double, ActionBuilder> actionBuilders) {
        actionBuilders.keySet().forEach(this::requirePositive);

        List<Tuple2<Double, ActionBuilder>> zero = List.of(Tuple.of(0.0, null));
        this.actionBuilders = actionBuilders.iterator()
                .foldLeft(zero, (list, t) -> list.append(Tuple.of(list.last()._1() + t._1(), t._2()))).tail();

        this.totalWeight = this.actionBuilders.last()._1();
    }

    private void requirePositive(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be positive: " + weight);
        }
    }

    public List<Tuple2<Double, ActionBuilder>> getCases() {
        return actionBuilders;
    }


    public double getTotalWeight() {
        return totalWeight;
    }

}
