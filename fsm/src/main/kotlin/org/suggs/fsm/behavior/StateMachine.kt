package org.suggs.fsm.behavior

import org.suggs.fsm.execution.NamespaceObjectMapper

class StateMachine(name: String,
                   val region: Region)
    : Behavior(name, {}){

    override fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper) {
        region.registerWithNamespace(namespaceContext)
    }
}