package org.suggs.fsm.behavior

class PseudoState(name: String,
                  val kind: PseudoStateKind)
    : Vertex(name) {

    override fun umlSyntax(): String = "[*]"


}
