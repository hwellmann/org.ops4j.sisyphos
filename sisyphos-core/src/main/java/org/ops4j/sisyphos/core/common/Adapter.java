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
package org.ops4j.sisyphos.core.common;

/**
 * Adapts a source type to a target type.
 *
 * @param <S>
 *            source type
 * @param <T>
 *            target type
 *
 * @author Harald Wellmann
 */
public interface Adapter<S, T> {

    /**
     * Adapts the source to a target type.
     *
     * @param source
     *            source object
     * @return target object
     */
    T adapt(S source);
}
