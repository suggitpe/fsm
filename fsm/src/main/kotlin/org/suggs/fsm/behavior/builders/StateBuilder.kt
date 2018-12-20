package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.State
import org.suggs.fsm.behavior.Vertex

class StateBuilder(name: String)
    : VertexBuilder(name) {

    companion object {
        fun aStateCalled(name: String): StateBuilder{
            return StateBuilder(name)
        }
    }

    override fun build(): Vertex {
        return State(name)
    }

}
