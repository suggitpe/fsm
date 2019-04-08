package org.suggs.fsm.stubs

import org.suggs.fsm.behavior.NamedElementContainer

class NamespaceStub(name: String) : NamedElementContainer(name, setOf()) {
    companion object {
        fun aNamespaceStub(): NamespaceStub {
            return NamespaceStub("Context")
        }
    }
}
