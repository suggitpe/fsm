package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Namespace
import org.suggs.fsm.behavior.Region

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

    fun build(container: Namespace): Region {
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
