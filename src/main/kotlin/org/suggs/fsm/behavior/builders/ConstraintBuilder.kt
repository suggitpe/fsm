package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Constraint
import org.suggs.fsm.behavior.SimpleGuardConstraint
import org.suggs.fsm.execution.BusinessEvent

class ConstraintBuilder(val name: String) {

    var guardCondition: (BusinessEvent) -> Boolean = { true }

    companion object {
        fun aConstraintCalled(name: String): ConstraintBuilder {
            return ConstraintBuilder(name)
        }
    }

    fun withGuardCondition(guardFunction: (BusinessEvent) -> Boolean): ConstraintBuilder {
        guardCondition = guardFunction
        return this
    }

    fun build(): Constraint {
        return SimpleGuardConstraint(name, guardCondition)
    }


}