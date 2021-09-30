package org.suggs.fsm.behavior.traits

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

interface Fireable {

    fun fire(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)

}