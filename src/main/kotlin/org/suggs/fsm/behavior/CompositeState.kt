package org.suggs.fsm.behavior

import org.suggs.fsm.behavior.traits.Namespace
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

class CompositeState(name: String,
                     container: Namespace,
                     deferrableTriggers: Set<Trigger>,
                     entryBehavior: Behavior,
                     exitBehavior: Behavior)
    : State(name, container, deferrableTriggers, entryBehavior, exitBehavior), Namespace {

    lateinit var region: Region
    override val ownedMembers: Set<NamedElement> = HashSet()

    override fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        throw NotImplementedError()
    }

    override fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        throw NotImplementedError()
    }

}