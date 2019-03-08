package org.suggs.fsm.behavior

import org.suggs.fsm.execution.NamespaceObjectMapper

class BehaviouredClassifier(name: String,
                            val ownedBehavior: Behaviour)
    : NamedElementContainer(name) {

    override fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper) {
        ownedBehavior.registerWithNamespace(namespaceContext)
    }

}
