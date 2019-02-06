package org.suggs.fsm.behavior

import org.suggs.fsm.behavior.traits.NamespaceRegisterable
import org.suggs.fsm.execution.NamespaceObjectMapper

abstract class NamedElement(val name: String)
    : NamespaceRegisterable {

    fun getQualifiedName(): String {
        return name
    }

    open fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper){
    }

    override fun registerWithNamespace(namespaceContext: NamespaceObjectMapper){
        namespaceContext.registerElement(this)
        registerMembersWithNamespace(namespaceContext)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NamedElement

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "NamedElement(name='$name')"
    }


}
