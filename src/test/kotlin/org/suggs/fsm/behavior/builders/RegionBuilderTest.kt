package org.suggs.fsm.behavior.builders

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.stubs.RegionContainerStub.Companion.aRegionContainerStub

class RegionBuilderTest {

    private val region = aRegionCalled("simple region")
            .withVertices(
                    anInitialPseudoStateCalled("INIT"),
                    aSimpleStateCalled("STATE_1"))
            .withTransitions(
                    aTransitionCalled("transition").startingAt("INIT").endingAt("STATE_1"))
            .build(aRegionContainerStub())

    @Test fun `can build an empty region`() {
        assertAll(
                Executable { assertThat(aRegionCalled("foo").build(aRegionContainerStub()).vertices).isEmpty() },
                Executable { assertThat(aRegionCalled("bar").build(aRegionContainerStub()).transitions).isEmpty() }
        )
    }

    @Test fun `builds regions with states and transitions`() {
        assertAll(
                Executable { assertThat(region.vertices.size).isEqualTo(2) },
                Executable { assertThat(region.transitions.size).isEqualTo(1) }
        )
    }

    @Test fun `joins transitions to vertexes`() {
        assertAll(
                Executable { assertThat(region.transitions.values.first().name).isEqualTo("transition") },
                Executable { assertThat(region.transitions.values.first().source.name).isEqualTo("INIT") },
                Executable { assertThat(region.transitions.values.first().target.name).isEqualTo("STATE_1") }
        )
    }

    @Test fun `joins vertices to transitions`() {
        assertAll(
                Executable { assertThat(region.vertices.getValue("INIT").incoming).hasSize(0) },
                Executable { assertThat(region.vertices.getValue("INIT").outgoing).hasSize(1) },
                Executable { assertThat(region.vertices.getValue("STATE_1").incoming).hasSize(1) },
                Executable { assertThat(region.vertices.getValue("STATE_1").outgoing).hasSize(0) }
        )
    }

}