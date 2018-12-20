package org.suggs.fsm.behavior

class FinalState(name: String)
    : State(name) {

    override fun umlSyntax(): String = "[*]"

}