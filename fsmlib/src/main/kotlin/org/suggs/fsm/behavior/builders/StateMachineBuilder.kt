package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.StateMachine
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled

class StateMachineBuilder(val name: String) {

    companion object {
        fun aStateMachineCalled(name: String): StateMachineBuilder {
            return StateMachineBuilder(name)
        }
    }

    var region: RegionBuilder = aRegionCalled("EmptyRegion")

    fun withRegion(region: RegionBuilder): StateMachineBuilder {
        this.region = region
        return this
    }

    fun build(): BehavioredClassifier {
        val stateMachine = StateMachine(name)
        val builtRegion = region.build(stateMachine)
        stateMachine.region = builtRegion
        return BehavioredClassifier("context", stateMachine)
    }


}