package org.suggs.fsm.engine.uml2.communications

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.communications.IEvent
import org.suggs.fsm.uml2.kernel.INamespace

class EventTest {

    private val EVENT_NAME = "test"

    private lateinit var event: IEvent
    @Mock lateinit var namespace: INamespace

    @BeforeEach
    internal fun `set up`() {
        initMocks(this)
        event = Event(EVENT_NAME, namespace)
    }

    /**
     * Tests that attributes are correctly set upon construction.
     */
    @Test
    fun `test construction`() {
        assertThat(event.name).isEqualTo(EVENT_NAME)
        assertThat(event.namespace).isEqualTo(namespace)
    }
}