package org.suggs.fsm.behavior

import org.suggs.fsm.execution.NamespaceObjectMapper

class BehavioredClassifier(name: String,
                           val stateMachine: StateMachine)
    : NamedElementContainer(name) {

    override fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper) {
        stateMachine.registerWithNamespace(namespaceContext)
    }

}
