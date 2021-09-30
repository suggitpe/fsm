package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.CompositeState
import org.suggs.fsm.behavior.PseudoStateKind.ENTRY_POINT
import org.suggs.fsm.behavior.PseudoStateKind.EXIT_POINT
import org.suggs.fsm.behavior.Region
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anInternalTransitionCalled
import org.suggs.fsm.behavior.traits.Enterable
import org.suggs.fsm.behavior.traits.Exitable

class CompositeStateBuilder(name: String) :
        SimpleStateBuilder(name) {

    lateinit var region: RegionBuilder
    private val entryPointBuilder: PseudoStateBuilder = PseudoStateBuilder(ENTRY_POINT)
    private val exitPointBuilder: PseudoStateBuilder = PseudoStateBuilder(EXIT_POINT)

    fun withRegion(aRegion: RegionBuilder): VertexBuilder {
        region = aRegion
        return this
    }

    override fun build(container: Region): Vertex {
        val compositeState = CompositeState(name,
                container,
                deferrableTriggers.map { it.build() }.toSet(),
                entryBehavior.build(),
                exitBehavior.build())
        region.withVertices(entryPointBuilder, exitPointBuilder)
        region.withTransitions(anInternalTransitionCalled("Default initial transition")
                .startingAt(entryPointBuilder.name)
                .endingAt(region.findInitialStateVertex().name))
        compositeState.region = region.build(compositeState)
        compositeState.entryPoint = compositeState.region.vertices[ENTRY_POINT.toString()] as Enterable
        compositeState.exitPoint = compositeState.region.vertices[EXIT_POINT.toString()] as Exitable
        return compositeState
    }
}
