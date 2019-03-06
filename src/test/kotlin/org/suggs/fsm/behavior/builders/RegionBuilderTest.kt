package org.suggs.fsm.behavior.builders

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.stubs.RegionContainerStub.Companion.aRegionContainerStub
import org.assertj.core.api.JUnitJupiterSoftAssertions
import org.junit.jupiter.api.extension.RegisterExtension



class RegionBuilderTest {

    @RegisterExtension
    val softly = JUnitJupiterSoftAssertions()

    private val region = aRegionCalled("simple region")
            .withVertices(
                    anInitialPseudoStateCalled("INIT"),
                    aSimpleStateCalled("STATE_1"))
            .withTransitions(
                    anExternalTransitionCalled("transition").startingAt("INIT").endingAt("STATE_1"))
            .build(aRegionContainerStub())

    @Test
    fun `can build an empty region`() {
        softly.assertThat(aRegionCalled("foo").build(aRegionContainerStub()).vertices).isEmpty()
        softly.assertThat(aRegionCalled("bar").build(aRegionContainerStub()).transitions).isEmpty()
    }

    @Test
    fun `builds regions with states and transitions`() {
        softly.assertThat(region.vertices.size).isEqualTo(2)
        softly.assertThat(region.transitions.size).isEqualTo(1)
    }

    @Test
    fun `joins transitions to vertexes`() {
        softly.assertThat(region.transitions.values.first().name).isEqualTo("transition")
        softly.assertThat(region.transitions.values.first().source.name).isEqualTo("INIT")
        softly.assertThat(region.transitions.values.first().target.name).isEqualTo("STATE_1")
    }

    @Test
    fun `joins vertices to transitions`() {
        softly.assertThat(region.vertices["INIT"]!!.incoming).hasSize(0)
        softly.assertThat(region.vertices["INIT"]!!.outgoing).hasSize(1)
        softly.assertThat(region.vertices["STATE_1"]!!.incoming).hasSize(1)
        softly.assertThat(region.vertices["STATE_1"]!!.outgoing).hasSize(0)
    }

}