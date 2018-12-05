package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.event.IEvent
import java.util.*

class EventContextTest {

    private val eventContext = EventContext()

    @Mock private lateinit var event: IEvent

    @BeforeEach
    fun `reset mocks`() {
        initMocks(this)
    }

    @Test fun `test accessors stupid test`() {
        val context = HashMap<String, String>()
        eventContext.context = context
        assertThat(eventContext.context).isEqualTo(context)

        eventContext.event = event
        assertThat(eventContext.event).isEqualTo(event)
    }
}