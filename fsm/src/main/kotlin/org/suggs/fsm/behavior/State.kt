package org.suggs.fsm.behavior

open class State(name: String)
    : Vertex(name) {

    override fun umlSyntax(): String = name

}
