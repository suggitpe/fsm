package org.suggs.fsm.behavior.builders

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled

class EventBuilderTest {

    private val event = anEventCalled("EVENT").build()

    @Test
    fun `events are built with a name`() {
        event.name shouldBe "EVENT"
    }
}