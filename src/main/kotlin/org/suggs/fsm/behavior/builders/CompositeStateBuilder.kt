package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.CompositeState
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
        val compositeState = CompositeState(name,
                container,
                deferrableTriggers.map { it.build() }.toSet(),
                entryBehavior.build(),
                exitBehavior.build())
        compositeState.region = region.build(compositeState)
        return compositeState
    }

}
