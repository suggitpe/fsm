package org.suggs.fsm.behavior

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

class ExternalTransition(name: String,
                         source: Vertex,
                         target: Vertex,
                         triggers: Set<Trigger> = HashSet(),
                         guard: Constraint = EmptyConstraint(),
                         effects: Set<Behavior> = HashSet())
    : Transition(name, source, target, triggers, guard, effects) {

    override fun doFire(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        // TODO: region exit for composite state in the right order
        // TOTO: least common ancestor

        source.exit(event, fsmExecutionContext)
        source.doExitAction(event, fsmExecutionContext)

        effects.map { it.execute(event) }

        target.doEntryAction(event, fsmExecutionContext)
        target.enter(event, fsmExecutionContext)
    }


}