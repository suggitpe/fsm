package org.suggs.fsm.engine.uml2.constraints.communications

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.suggs.fsm.uml2.communications.ITrigger

class TriggerCheckerTest {

    private lateinit var triggerChecker: ITriggerChecker

    @BeforeEach fun setUp() {
        triggerChecker = TriggerChecker()
    }

    @Test fun testChecksNonNullEvent() {
        val trigger = mock(ITrigger::class.java)
        `when`(trigger.getEvent()).thenReturn(null)

        assertThrows<IllegalStateException> { triggerChecker.checkConstraints(trigger) }
    }
}