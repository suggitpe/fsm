package org.suggs.fsm.stubs

import org.suggs.fsm.execution.BusinessObjectReference

class BusinessObjectReferenceStub {

    companion object {
        fun aBOReferenceForTest() = BusinessObjectReference("", "", 0)
    }
}