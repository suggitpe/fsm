package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.PseudoStateKind
import org.suggs.fsm.behavior.Region
import org.suggs.fsm.behavior.traits.RegionContainer

class RegionBuilder(val name: String) {
    companion object {

        fun aRegionCalled(name: String): RegionBuilder {
            return RegionBuilder(name)
        }
    }

    private val vertexBuilders: MutableSet<VertexBuilder> = HashSet()
    private val transitionBuilders: MutableSet<TransitionBuilder> = HashSet()

    fun withVertices(vararg vertices: VertexBuilder): RegionBuilder {
        this.vertexBuilders.addAll(vertices)
        return this
    }

    fun withTransitions(vararg transitions: TransitionBuilder): RegionBuilder {
        this.transitionBuilders.addAll(transitions)
        return this
    }

    fun findInitialStateVertex(): VertexBuilder {
        val initialStateBuilder = vertexBuilders.find { it is PseudoStateBuilder && it.pseudoStateKind == PseudoStateKind.INITIAL }
        if (initialStateBuilder != null) return initialStateBuilder
        else throw IllegalStateException("Could not find initial state builder in region builder $name")
    }

    fun build(container: RegionContainer): Region {
        val region = Region(name, container)
        val vertices = vertexBuilders.map { it.name to it.build(region) }.toMap()
        val transitions = transitionBuilders.map { it.name to it.build(vertices) }.toMap()
        transitions.values.map {
            vertices.getValue(it.source.name).addOutgoingTransition(it)
            vertices.getValue(it.target.name).addIncomingTransition(it)
        }
        region.vertices = vertices
        region.transitions = transitions
        return region
    }

}
