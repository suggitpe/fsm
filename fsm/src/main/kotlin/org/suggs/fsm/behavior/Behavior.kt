package org.suggs.fsm.behavior

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmStateManager
import org.suggs.fsm.execution.NamespaceObjectMapper

open class Behavior(name: String,
                    private val executableFunction: (BusinessEvent) -> Unit)
    : Namespace(name) {

    fun execute(context: BusinessEvent) {
        executableFunction(context)
    }
}

class EmptyBehavior : Behavior("EMPTY", {})
