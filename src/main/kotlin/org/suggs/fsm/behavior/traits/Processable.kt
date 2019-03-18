package org.suggs.fsm.behavior.traits

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

interface Processable {

    fun processEvent(event: BusinessEvent,
                     fsmExecutionContext: FsmExecutionContext)

}