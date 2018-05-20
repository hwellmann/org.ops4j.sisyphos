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
package org.ops4j.sisyphos.core.session;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.Test;
import org.ops4j.sisyphos.api.action.Group;
import org.ops4j.sisyphos.api.session.Status;

import io.vavr.collection.HashMap;

/**
 * Unit tests for {@link SessionImpl}.
 *
 * @author Harald Wellmann
 *
 */
public class SessionImplTest {

    @Test
    public void shouldCreateSessionWithUserId() {
        long now = System.currentTimeMillis();
        ExtendedSession session = new SessionImpl(17);

        assertThat(session.getUserId()).isEqualTo(17);
        assertThat(session.getScenario()).isEmpty();
        assertThat(session.getStartDate()).isGreaterThanOrEqualTo(now);
        assertThat(session.getStatus()).isEqualTo(Status.OK);
        assertThat(session.getAttributeKeys()).isEmpty();
        assertThat(session.getGroupHierarchy()).isEmpty();
    }

    @Test
    public void shouldCreateSessionWithScenario() {
        long now = System.currentTimeMillis();
        ExtendedSession session = new SessionImpl("foo");

        assertThat(session.getUserId()).isEqualTo(0);
        assertThat(session.getScenario()).isEqualTo("foo");
        assertThat(session.getStartDate()).isGreaterThanOrEqualTo(now);
        assertThat(session.getStatus()).isEqualTo(Status.OK);
        assertThat(session.getAttributeKeys()).isEmpty();
        assertThat(session.getGroupHierarchy()).isEmpty();
    }

    @Test
    public void shouldCreateSessionWithScenarioAndUserId() {
        long now = System.currentTimeMillis();
        ExtendedSession session = new SessionImpl("bar", 23);

        assertThat(session.getUserId()).isEqualTo(23);
        assertThat(session.getScenario()).isEqualTo("bar");
        assertThat(session.getStartDate()).isGreaterThanOrEqualTo(now);
        assertThat(session.getStatus()).isEqualTo(Status.OK);
        assertThat(session.getAttributeKeys()).isEmpty();
        assertThat(session.getGroupHierarchy()).isEmpty();
    }

    @Test
    public void shouldCreateSessionWithScenarioAndUserIdAndAttributes() {
        long now = System.currentTimeMillis();
        Map<String, Object> attrs = HashMap.<String, Object>of("a", 12, "b", "foo").toJavaMap();
        ExtendedSession session = new SessionImpl("bar", 23, attrs);

        assertThat(session.getUserId()).isEqualTo(23);
        assertThat(session.getScenario()).isEqualTo("bar");
        assertThat(session.getStartDate()).isGreaterThanOrEqualTo(now);
        assertThat(session.getStatus()).isEqualTo(Status.OK);
        assertThat(session.getGroupHierarchy()).isEmpty();
        assertThat(session.getAttributeKeys()).containsExactlyInAnyOrder("a", "b");
        assertThat(session.<Integer>getAttribute("a")).isEqualTo(12);
        assertThat(session.<String>getAttribute("b")).isEqualTo("foo");
    }

    @Test
    public void shouldModifyAttribute() {
        Map<String, Object> attrs = HashMap.<String, Object>of("a", 12, "b", "foo").toJavaMap();
        ExtendedSession session = new SessionImpl("bar", 23, attrs);

        session.setAttribute("a", "blah");
        assertThat(session.<String>getAttribute("a")).isEqualTo("blah");

    }

    @Test
    public void shouldRemoveAttribute() {
        Map<String, Object> attrs = HashMap.<String, Object>of("a", 12, "b", "foo").toJavaMap();
        ExtendedSession session = new SessionImpl("bar", 23, attrs);

        Integer removed = session.removeAttribute("a");
        assertThat(removed).isEqualTo(12);
        assertThat(session.<Integer>getAttribute("a")).isNull();
        assertThat(session.getAttributeKeys()).containsExactly("b");

    }

    @Test
    public void shouldAddAttribute() {
        Map<String, Object> attrs = HashMap.<String, Object>of("a", 12, "b", "foo").toJavaMap();
        ExtendedSession session = new SessionImpl("bar", 23, attrs);

        LocalDateTime dateTime = LocalDateTime.now();
        session.setAttribute("c", dateTime);
        assertThat(session.<LocalDateTime>getAttribute("c")).isEqualTo(dateTime);
        assertThat(session.getAttributeKeys()).containsExactlyInAnyOrder("a", "b", "c");
    }

    @Test
    public void shouldStartAndEndGroups() {
        ExtendedSession session = new SessionImpl("bar", 23);
        Group g1 = session.startGroup("g1");
        session.accumulateResponseTime(100);
        session.accumulateResponseTime(200);
        assertThat(g1.getName()).isEqualTo("g1");
        assertThat(session.getGroupHierarchy()).containsExactly("g1");
        assertThat(g1.getCumulatedResponseTime()).isEqualTo(300);

        Group g2 = session.startGroup("g2");
        assertThat(g2.getName()).isEqualTo("g2");
        assertThat(session.getGroupHierarchy()).containsExactly("g2", "g1");
        session.accumulateResponseTime(300);
        session.accumulateResponseTime(400);
        assertThat(g1.getCumulatedResponseTime()).isEqualTo(300);
        assertThat(g2.getCumulatedResponseTime()).isEqualTo(700);


        Group g2End = session.endGroup("g2");
        assertThat(g2End).isEqualTo(g2);
        assertThat(session.getGroupHierarchy()).containsExactly("g1");
        assertThat(g1.getCumulatedResponseTime()).isEqualTo(1000);
        assertThat(g2.getCumulatedResponseTime()).isEqualTo(700);

        Group g1End = session.endGroup("g1");
        assertThat(g1End).isEqualTo(g1);
        assertThat(session.getGroupHierarchy()).isEmpty();
        assertThat(g1.getCumulatedResponseTime()).isEqualTo(1000);
    }

    @Test
    public void shouldNotStartAndEndGroupsInWrongOrder() {
        ExtendedSession session = new SessionImpl("bar", 23);
        session.startGroup("g1");
        assertThat(session.getGroupHierarchy()).containsExactly("g1");
        session.startGroup("g2");
        assertThat(session.getGroupHierarchy()).containsExactly("g2", "g1");

        assertThatIllegalArgumentException().isThrownBy(() -> session.endGroup("g1"));
    }

    @Test
    public void shouldNotEndGroupBeforeStarting() {
        ExtendedSession session = new SessionImpl("bar", 23);

        assertThatIllegalArgumentException().isThrownBy(() -> session.endGroup("g1"));
    }

    @Test
    public void shouldNotStartGroupTwice() {
        ExtendedSession session = new SessionImpl("bar", 23);
        session.startGroup("g1");
        assertThat(session.getGroupHierarchy()).containsExactly("g1");
        session.startGroup("g2");
        assertThat(session.getGroupHierarchy()).containsExactly("g2", "g1");

        assertThatIllegalArgumentException().isThrownBy(() -> session.startGroup("g1"));
    }


    @Test
    public void shouldMarkAsFailed() {
        ExtendedSession session = new SessionImpl("bar", 23);
        session.markAsFailed();
        assertThat(session.getStatus()).isEqualTo(Status.KO);

        session.setStatus(Status.OK);
        assertThat(session.getStatus()).isEqualTo(Status.OK);

    }

    @Test
    public void shouldIgnoreResponseTimeForEmptyGroupStack() {
        ExtendedSession session = new SessionImpl("bar", 23);
        session.accumulateResponseTime(100);
        assertThat(session.getGroupHierarchy()).isEmpty();
    }

}
