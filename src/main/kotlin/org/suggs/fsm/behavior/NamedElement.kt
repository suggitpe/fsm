package org.suggs.fsm.behavior

import org.suggs.fsm.behavior.traits.Named
import org.suggs.fsm.behavior.traits.Namespace
import org.suggs.fsm.behavior.traits.NamespaceRegisterable
import org.suggs.fsm.execution.NamespaceObjectMapper

abstract class NamedElement(override val name: String)
    : NamespaceRegisterable, Named {

    private var namespace: Namespace? = null
    private var qualifiedName: String = ""

    constructor(name: String, namespace: Namespace?) : this(name) {
        this.namespace = namespace
    }

    override fun deriveQualifiedName(): String {
        if (qualifiedName.isNullOrBlank()) {

            qualifiedName = when {
                namespace == null -> name
                namespace!!.name.isBlank() -> ""
                else -> namespace!!.deriveQualifiedName() + "::" + name
            }
        }
        return qualifiedName
    }

    open fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper) {
    }

    override fun registerWithNamespace(namespaceContext: NamespaceObjectMapper) {
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
