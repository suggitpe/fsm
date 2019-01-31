package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.FinalState
import org.suggs.fsm.behavior.Region
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.builders.EmptyBehaviourBuilder.Companion.anEmptyBehavior

class FinalStateBuilder(name: String)
    : VertexBuilder(name) {

    override fun withDeferrableTriggers(vararg newTriggers: TriggerBuilder): VertexBuilder {
        throw IllegalStateException("You cannot set deferrable triggers on a final state")
    }

    override fun withEntryBehavior(behavior: BehaviorBuilder): VertexBuilder {
        throw IllegalStateException("You cannot define entry behaviors on final states")
    }

    override fun withExitBehavior(behavior: BehaviorBuilder): VertexBuilder {
        throw IllegalStateException("You cannot define exit behaviors on final states")
    }

    override fun build(region: Region): Vertex {
        return FinalState(name,
                region,
                anEmptyBehavior().build(),
                anEmptyBehavior().build())
    }

}
