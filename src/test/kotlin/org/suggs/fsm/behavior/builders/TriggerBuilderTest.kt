package org.suggs.fsm.behavior.builders

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled

class TriggerBuilderTest {

    private val trigger = aTriggerCalled("TRIGGER1")
            .firedWith(anEventCalled("EVENT"))
            .build()

    @Test
    fun `triggers are built with events`() {
        assertThat(trigger.event.name).isEqualTo("EVENT")
    }
}
