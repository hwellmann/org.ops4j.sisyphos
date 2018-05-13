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

import org.junit.Test;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

/**
 * @author Harald Wellmann
 *
 */
public class EmitterTest {

    @Test
    public void shouldEmit() throws InterruptedException {
        EmitterProcessor<String> processor = EmitterProcessor.create();
        FluxSink<String> emitter = processor.sink();

        StepVerifier.create(processor).then(() -> {
          emitter.next("one");
          emitter.next("two");
          emitter.next("three");
          emitter.complete();

        }).expectNext("one", "two", "three").verifyComplete();
    }

    @Test
    public void shouldEmitError() throws InterruptedException {
        EmitterProcessor<String> processor = EmitterProcessor.create();
        FluxSink<String> emitter = processor.sink();

        StepVerifier.create(processor).then(() -> {
          emitter.next("one");
          emitter.next("two");
          emitter.error(new IllegalStateException());
          emitter.complete();

        }).expectNext("one", "two").verifyError(IllegalStateException.class);
    }
}
