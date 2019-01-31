package org.suggs.fsm.behavior.traits

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext
import org.suggs.fsm.execution.NamespaceObjectMapper

interface Fireable {

    fun fire(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)

}