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
package org.ops4j.sisyphos.core.builder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.vavr.collection.List;

/**
 * @author Harald Wellmann
 *
 */
public class RandomSwitchBuilderTest {

    @Test
    public void random() {
//        DirectActionBuilder ab1 = new DirectActionBuilder(null);
//        DirectActionBuilder ab2 = new DirectActionBuilder(null);
//        RandomSwitchBuilder switchBuilder = new RandomSwitchBuilder(LinkedHashMap.of(2.0, ab1, 1.0, ab2));
//        switchBuilder.setRandom(new Random(20011));
//
//        List<ActionBuilder> builders = List.empty();
//        for (int i = 0; i < 20; i++) {
//            builders = builders.append(switchBuilder.selectRandom());
//        }
//
//        assertThat(builders).containsExactly(
//                ab1, ab2, ab1, ab1, ab1, ab2, ab1, ab1, ab1, ab1,
//                ab2, ab1, ab1, ab2, ab1, ab1, ab1, ab2, ab2, ab1);
    }

    @Test
    public void partialSums() {
        List<Integer> numbers = List.of(1, 3, 5, 7, 9);
        assertThat(numbers.foldLeft(List.of(0), (list, n) -> list.append(list.last() + n)).tail())
            .containsExactly(1, 4, 9, 16, 25);
    }
}
