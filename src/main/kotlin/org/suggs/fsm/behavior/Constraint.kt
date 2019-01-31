package org.suggs.fsm.behavior

import org.suggs.fsm.execution.BusinessEvent

interface Constraint {

    fun evaluate(event: BusinessEvent): Boolean
}

class EmptyConstraint : Constraint {
    override fun evaluate(event: BusinessEvent): Boolean {
        return true
    }

}
