package org.suggs.fsm.stubs

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.BusinessObjectIdentifier

class BusinessEventStub {

    companion object {
        fun aBusinessEventCalled(name: String): BusinessEvent {
            return BusinessEvent(name, BusinessObjectIdentifier("", "", 0))
        }
    }
}