package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Behavior
import org.suggs.fsm.execution.BusinessEvent

open class BehaviorBuilder(val name: String) {

    companion object {
        fun aBehaviorCalled(name: String): BehaviorBuilder {
            return BehaviorBuilder(name)
        }

    }

    private var executableFunction: (BusinessEvent) -> Unit = {}

    fun withAction(effect: (BusinessEvent) -> Unit): BehaviorBuilder {
        executableFunction = effect
        return this
    }

    fun build(): Behavior {
        return Behavior(name, executableFunction)
    }

}

class EmptyBehaviourBuilder()
    : BehaviorBuilder("EMPTY") {

    companion object {
        fun anEmptyBehavior(): EmptyBehaviourBuilder {
            return EmptyBehaviourBuilder()
        }
    }
}