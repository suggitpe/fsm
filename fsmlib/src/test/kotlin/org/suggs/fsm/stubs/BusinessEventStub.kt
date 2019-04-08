package org.suggs.fsm.stubs

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.stubs.BusinessObjectReferenceStub.Companion.aBOReferenceForTest

class BusinessEventStub {

    companion object {
        fun aBusinessEventCalled(name: String): BusinessEvent {
            return BusinessEvent(name, aBOReferenceForTest())
        }
    }
}