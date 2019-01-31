package org.suggs.fsm.behavior.traits

import org.suggs.fsm.execution.NamespaceObjectMapper

interface NamespaceRegisterable {

    fun registerWithNamespace(namespaceContext: NamespaceObjectMapper)
}