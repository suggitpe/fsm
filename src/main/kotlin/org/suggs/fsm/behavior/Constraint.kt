package org.suggs.fsm.behavior

import org.suggs.fsm.execution.BusinessEvent

interface Constraint {

    fun evaluate(event: BusinessEvent): Boolean
}

class EmptyGuardConstraint : Constraint {
    override fun evaluate(event: BusinessEvent): Boolean {
        return true
    }
}

class SimpleGuardConstraint(private val guardFunction: (BusinessEvent) -> Boolean) : Constraint {
    override fun evaluate(event: BusinessEvent): Boolean {
        return guardFunction(event)
    }
}
