package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.PseudoState
import org.suggs.fsm.behavior.PseudoStateKind
import org.suggs.fsm.behavior.Vertex

class PseudoStateBuilder(name: String,
                         val pseudoStateKind: PseudoStateKind)
    : VertexBuilder(name) {

    override fun build(): Vertex {
        return PseudoState(name, pseudoStateKind)
    }

}
