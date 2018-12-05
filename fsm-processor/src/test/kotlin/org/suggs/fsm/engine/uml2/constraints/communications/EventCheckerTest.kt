package org.suggs.fsm.engine.uml2.constraints.communications

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.suggs.fsm.engine.uml2.communications.Event
import org.suggs.fsm.engine.uml2.kernel.Namespace
import org.suggs.fsm.uml2.communications.IEvent

class EventCheckerTest {
    private lateinit var eventChecker: IEventChecker
    private lateinit var event: IEvent

    @BeforeEach fun setUp() {
        eventChecker = EventChecker()
        event = Event("MyEvent", Namespace())
    }

    @Test fun testConstraintsCheck() {
        eventChecker.checkConstraints(event)
    }
}