package org.suggs.fsm.behavior

import org.suggs.fsm.behavior.traits.Named
import org.suggs.fsm.execution.BusinessEvent

interface Constraint : Named {
    fun evaluate(event: BusinessEvent): Boolean
}

class EmptyGuardConstraint : NamedElement("EMPTY"), Constraint {
    override fun evaluate(event: BusinessEvent): Boolean {
        return true
    }
}

class SimpleGuardConstraint(name: String, private val guardFunction: (BusinessEvent) -> Boolean) : NamedElement(name), Constraint {
    override fun evaluate(event: BusinessEvent): Boolean {
        return guardFunction(event)
    }
}
