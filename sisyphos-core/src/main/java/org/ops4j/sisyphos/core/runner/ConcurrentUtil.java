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
package org.ops4j.sisyphos.core.runner;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Harald Wellmann
 *
 */
public final class ConcurrentUtil {

    private static Logger log = LoggerFactory.getLogger(ConcurrentUtil.class);


    private ConcurrentUtil() {
        // hidden constructor
    }

    public static void waitFor(CountDownLatch latch) {
        try {
            latch.await();
        }
        catch (InterruptedException exc) {
            log.debug("Interrupted latch", exc);
        }
    }
}
