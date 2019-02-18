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
    lateinit var entryPoint: PseudoState
    lateinit var exitPoint: PseudoState
    override val ownedMembers: Set<NamedElement> = HashSet()

    override fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        fsmExecutionContext.stateManager.storeActiveState(deriveQualifiedName())
        entryPoint.enter(event, fsmExecutionContext)
    }

    override fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        exitPoint.exit(event, fsmExecutionContext)
    }

}