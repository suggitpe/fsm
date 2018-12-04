package org.suggs.fsm.engine.impl

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.event.IEvent
import org.suggs.fsm.uml2.kernel.INamespace.NAMESPACE_SEPARATOR
import org.suggs.fsm.uml2.kernel.INamespace.TOP_LEVEL_NAMESPACE

class DefaultEventMapperTest {

    private val eventType = TOP_LEVEL_NAMESPACE + NAMESPACE_SEPARATOR + "context" + NAMESPACE_SEPARATOR + "event1"
    private val eventMapper = DefaultEventMapper()

    @Mock lateinit var event: IEvent

    @BeforeEach fun `reset mocks`() {
        initMocks(this)
    }

    /**
     * Tests that the DefaultEventMapper does not changed the event typeif the event name already
     * has the top::{behaviored classifer name}:: prefix.
     */
    @Test fun `retains event type when set correctly`() {
        `when`(event.type).thenReturn(eventType)
        eventMapper.map(event)
        verify(event, atLeastOnce()).type
    }

    /**
     * Tests that the DefaultEventMapper appends the prefix top::{behaviored classifer name}:: to the
     * event type if doesn't already have it.
     */
    @Test fun `corrects event type when prefix omitted`() {
        `when`(event.type).thenReturn("event1")
        eventMapper.map(event)
        verify(event).type = eventType
    }
}