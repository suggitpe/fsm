package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.NamedElementContainer
import org.suggs.fsm.behavior.State
import org.suggs.fsm.behavior.Vertex

class CompositeStateBuilder(name: String) :
        StateBuilder(name) {
    lateinit var region: RegionBuilder

    fun withRegion(aRegion: RegionBuilder): VertexBuilder {
        region = aRegion
        return this
    }

    override fun build(container: NamedElementContainer): Vertex {
        return State(name,
                container,
                deferrableTriggers.map { it.build() }.toSet(),
                entryBehavior.build(),
                exitBehavior.build())
    }

}
