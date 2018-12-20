package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.FinalState
import org.suggs.fsm.behavior.Vertex

class FinalStateBuilder(name: String)
    : VertexBuilder(name) {

    override fun build(): Vertex {
        return FinalState(name)
    }
}
