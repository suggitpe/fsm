package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.StateMachine

class StateMachineBuilder(val name: String)
    : Builder<BehavioredClassifier> {

    companion object {
        fun aStateMachineCalled(name: String): StateMachineBuilder {
            return StateMachineBuilder(name)
        }
    }

    var region: RegionBuilder? = null

    fun withRegion(region: RegionBuilder): StateMachineBuilder {
        this.region = region
        return this
    }

    override fun build(): BehavioredClassifier {
        val stateMachine = StateMachine(name, region!!.build())
        return BehavioredClassifier("context", stateMachine)
    }


}