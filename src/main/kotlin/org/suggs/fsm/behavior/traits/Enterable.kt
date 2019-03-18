package org.suggs.fsm.behavior.traits

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

interface Enterable {

    fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)

    fun doEntryAction(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)
}