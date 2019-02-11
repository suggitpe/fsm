package org.suggs.fsm.behavior

import org.suggs.fsm.execution.NamespaceObjectMapper

class StateMachine(name: String)
    : Behavior(name, {}) {

    lateinit var region: Region

    override fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper) {
        region.registerWithNamespace(namespaceContext)
    }
}