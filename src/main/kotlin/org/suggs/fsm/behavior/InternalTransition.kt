package org.suggs.fsm.behavior

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

class InternalTransition(name: String,
                         source: Vertex,
                         target: Vertex,
                         triggers: Set<Trigger> = HashSet(),
                         guard: Constraint = EmptyConstraint(),
                         effects: Set<Behavior> = HashSet())
    : Transition(name, source, target, triggers, guard, effects) {

    override fun doFire(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        source.exit(event, fsmExecutionContext)

        effects.map { it.execute(event) }

        target.enter(event, fsmExecutionContext)
    }


}