package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Region

class RegionBuilder(val name: String)
    : Builder<Region> {
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

    override fun build(): Region {
        val vertices = vertexBuilders.map { it.name to it.build() }.toMap()
        val transitions = transitionBuilders.map { it.name to it.build(vertices) }.toMap()
        transitions.values.map {
            vertices[it.source.name]!!.addOutgoingTransition(it)
            vertices[it.target.name]!!.addIncomingTransition(it)
        }
        return Region(name, vertices, transitions)
    }

}
