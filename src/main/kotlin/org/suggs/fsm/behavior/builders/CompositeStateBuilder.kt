package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.NamedElementContainer
import org.suggs.fsm.behavior.SimpleState
import org.suggs.fsm.behavior.Vertex

class CompositeStateBuilder(name: String) :
        SimpleStateBuilder(name) {
    lateinit var region: RegionBuilder

    fun withRegion(aRegion: RegionBuilder): VertexBuilder {
        region = aRegion
        return this
    }

    override fun build(container: NamedElementContainer): Vertex {
        return SimpleState(name,
                container,
                deferrableTriggers.map { it.build() }.toSet(),
                entryBehavior.build(),
                exitBehavior.build())
    }

}
