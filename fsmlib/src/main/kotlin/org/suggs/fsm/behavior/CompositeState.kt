package org.suggs.fsm.behavior

import org.suggs.fsm.behavior.traits.Enterable
import org.suggs.fsm.behavior.traits.Exitable
import org.suggs.fsm.behavior.traits.RegionContainer
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext
import org.suggs.fsm.execution.NamespaceObjectMapper

class CompositeState(name: String,
                     container: Region,
                     deferrableTriggers: Set<Trigger>,
                     entryBehavior: Behavior,
                     exitBehavior: Behavior)
    : State(name, container, deferrableTriggers, entryBehavior, exitBehavior), RegionContainer {

    lateinit var region: Region
    lateinit var entryPoint: Enterable
    lateinit var exitPoint: Exitable
    override val ownedMembers: Set<NamedElement> = HashSet()

    override fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper) {
        region.registerWithNamespace(namespaceContext)
    }

    override fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        fsmExecutionContext.stateManager.storeActiveState(deriveQualifiedName())
        entryPoint.enter(event, fsmExecutionContext)
    }

    override fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        exitPoint.exit(event, fsmExecutionContext)
    }

    override fun findInheritedFireableTransitionsFor(event: BusinessEvent): Set<Transition> {
        return super.findFireableTransitionsFor(event)
    }

}