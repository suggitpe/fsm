package org.suggs.fsm.engine.uml2.communications

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.communications.IEvent
import org.suggs.fsm.uml2.communications.ITrigger

class TriggerTest {
    private lateinit var trigger: ITrigger
    @Mock lateinit var mockEvent: IEvent

    @BeforeEach
    fun `set up`() {
        initMocks(this)
        trigger = Trigger()
    }

    @Test
    fun `test accessors`() {
        trigger.event = mockEvent
        assertThat(trigger.event).isEqualTo(mockEvent)
    }

    /**
     * Test that the default trigger uses a completion event
     */
    @Test
    fun `test default event creation trigger`() {
        assertThat(trigger.event.qualifiedName).isEqualTo(IEvent.COMPLETION_EVENT_QUALIFIED_NAME)
    }
}