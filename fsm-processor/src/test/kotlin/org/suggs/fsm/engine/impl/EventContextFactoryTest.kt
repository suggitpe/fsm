package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.event.IEvent
import java.util.*

class EventContextFactoryTest {
    private val eventType = "top::EVT_TYPE"
    private val eventContextFactory = EventContextFactory()

    @Mock lateinit var event: IEvent

    @BeforeEach fun `reset mocks`() {
        initMocks(this)
    }

    @Test fun `creates an event context`() {
        `when`(event.type).thenReturn(eventType)
        val mockContext = HashMap<String, String>()
        val eventContext = eventContextFactory.createEventContext(event, mockContext, null)

        assertThat(eventContext.event.type).isEqualTo(eventType)
        assertThat(eventContext.context).isEqualTo(mockContext)
    }
}