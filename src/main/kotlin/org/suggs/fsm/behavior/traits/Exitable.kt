package org.suggs.fsm.behavior.traits

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

interface Exitable {

    fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)

    fun doExitAction(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)

}