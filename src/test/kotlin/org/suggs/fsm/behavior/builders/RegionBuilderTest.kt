package org.suggs.fsm.behavior.builders

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled

class RegionBuilderTest {

    private val region = aRegionCalled("simple region")
            .withVertices(
                    anInitialPseudoStateCalled("INIT"),
                    aSimpleStateCalled("STATE_1"))
            .withTransitions(
                    anExternalTransitionCalled("start").startingAt("INIT").endingAt("STATE_1"))
            .build()

    @Test
    fun `can build an empty region`() {
        assertThat(aRegionCalled("foo").build().vertices).isEmpty()
        assertThat(aRegionCalled("bar").build().transitions).isEmpty()
    }

    @Test
    fun `builds regions with states and transitions`() {
        assertThat(region.vertices.size).isEqualTo(2)
        assertThat(region.transitions.size).isEqualTo(1)
    }

    @Test
    fun `joins transitions to vertexes`() {
        region.transitions.values.map { assertThat(it.source != null) }
        region.transitions.values.map { assertThat(it.target != null) }
    }

    @Test
    fun `joins vertices to transitions`() {
        assertThat(region.vertices["INIT"]!!.incoming).hasSize(0)
        assertThat(region.vertices["INIT"]!!.outgoing).hasSize(1)
        assertThat(region.vertices["STATE_1"]!!.incoming).hasSize(1)
        assertThat(region.vertices["STATE_1"]!!.outgoing).hasSize(0)
    }

}