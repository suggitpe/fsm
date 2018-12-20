package org.suggs.fsm.behavior

class Trigger(name: String, val event: Event)
    : NamedElement(name) {

    override fun umlSyntax(): String {
        return ""
    }

}
