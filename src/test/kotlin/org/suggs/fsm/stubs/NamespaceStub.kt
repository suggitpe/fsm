package org.suggs.fsm.stubs

import org.suggs.fsm.behavior.Namespace

class NamespaceStub(name: String) : Namespace(name, setOf()) {
    companion object {
        fun aNamespaceStub(): NamespaceStub {
            return NamespaceStub("Context")
        }
    }
}
