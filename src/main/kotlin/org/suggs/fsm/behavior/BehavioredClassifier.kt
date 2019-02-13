package org.suggs.fsm.behavior

import org.suggs.fsm.execution.NamespaceObjectMapper

class BehavioredClassifier(name: String,
                           val ownedBehavior: Behavior)
    : NamedElementContainer(name) {

    override fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper) {
        ownedBehavior.registerWithNamespace(namespaceContext)
    }

}
