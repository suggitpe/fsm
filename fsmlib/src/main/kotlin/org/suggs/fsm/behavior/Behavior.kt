package org.suggs.fsm.behavior

import org.suggs.fsm.execution.BusinessEvent

open class Behavior(name: String,
                    private val executableFunction: (BusinessEvent) -> Unit)
    : NamedElementContainer(name) {

    fun execute(context: BusinessEvent) {
        executableFunction(context)
    }
}