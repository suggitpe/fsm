package org.suggs.fsm.behavior

import org.suggs.fsm.behavior.traits.RegionContainer
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.NamespaceObjectMapper

class StateMachine(name: String)
    : Behaviour(name, {}), RegionContainer {

    lateinit var region: Region

    override fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper) {
        region.registerWithNamespace(namespaceContext)
    }

    override fun findInheritedFireableTransitionsFor(event: BusinessEvent): Set<Transition> {
        return setOf()
    }
}