package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.State
import org.suggs.fsm.behavior.Vertex

class CompositeStateBuilder(name: String)
    : VertexBuilder(name) {

    lateinit var region: RegionBuilder

    fun withRegion(aRegion: RegionBuilder): VertexBuilder {
        region = aRegion
        return this
    }

    override fun build(): Vertex {
        return State(name)
    }

}
