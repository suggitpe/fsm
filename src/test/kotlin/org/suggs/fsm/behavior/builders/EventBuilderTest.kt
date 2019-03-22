package org.suggs.fsm.behavior.builders

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled

class EventBuilderTest {

    private val event = anEventCalled("EVENT").build()

    @Test fun `events are built with a name`() {
        assertThat(event.name).isEqualTo("EVENT")
    }
}