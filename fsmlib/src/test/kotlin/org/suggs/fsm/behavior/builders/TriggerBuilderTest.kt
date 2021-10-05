package org.suggs.fsm.behavior.builders

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled

class TriggerBuilderTest {

    private val trigger = aTriggerCalled("TRIGGER1")
        .firedWith(anEventCalled("EVENT"))
        .build()

    @Test
    fun `triggers are built with events`() {
        trigger.event.name shouldBe "EVENT"
    }
}
