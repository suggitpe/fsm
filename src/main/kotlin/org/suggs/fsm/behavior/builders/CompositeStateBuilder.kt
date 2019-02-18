package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.CompositeState
import org.suggs.fsm.behavior.PseudoStateKind
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.traits.Namespace

class CompositeStateBuilder(name: String) :
        SimpleStateBuilder(name) {
    lateinit var region: RegionBuilder
    private val entryPoint: PseudoStateBuilder = PseudoStateBuilder("", PseudoStateKind.ENTRY_POINT)
    private val exitPoint: PseudoStateBuilder = PseudoStateBuilder("", PseudoStateKind.EXIT_POINT)

    fun withRegion(aRegion: RegionBuilder): VertexBuilder {
        region = aRegion
        return this
    }


    override fun build(container: Namespace): Vertex {
        val compositeState = CompositeState(name,
                container,
                deferrableTriggers.map { it.build() }.toSet(),
                entryBehavior.build(),
                exitBehavior.build())
        compositeState.region = region.build(compositeState)
        compositeState.entryPoint = entryPoint.build(compositeState)
        compositeState.exitPoint = exitPoint.build(compositeState)
        return compositeState
    }

}
