package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Behavior
import org.suggs.fsm.behavior.EventContext

open class BehaviorBuilder(val name: String)
    : Builder<Behavior> {

    companion object {
        fun aBehaviorCalled(name: String): BehaviorBuilder {
            return BehaviorBuilder(name)
        }

    }

    private var executableFunction: (EventContext) -> Unit = {}

    fun withAction(effect: (EventContext) -> Unit): BehaviorBuilder {
        executableFunction = effect
        return this
    }

    override fun build(): Behavior {
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