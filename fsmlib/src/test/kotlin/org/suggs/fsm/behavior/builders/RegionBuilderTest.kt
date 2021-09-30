package org.suggs.fsm.behavior.builders

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.stubs.RegionContainerStub.Companion.aRegionContainerStub

class RegionBuilderTest {

    private val region = aRegionCalled("simple region")
        .withVertices(
            anInitialPseudoStateCalled("INIT"),
            aSimpleStateCalled("STATE_1")
        )
        .withTransitions(
            aTransitionCalled("transition").startingAt("INIT").endingAt("STATE_1")
        )
        .build(aRegionContainerStub())

    @Test
    fun `can build an empty region`() {
        assertSoftly {
            aRegionCalled("foo").build(aRegionContainerStub()).vertices shouldBe emptyMap()
            aRegionCalled("bar").build(aRegionContainerStub()).transitions shouldBe emptyMap()
        }
    }

    @Test
    fun `builds regions with states and transitions`() {
        assertSoftly {
            region.vertices.size shouldBe 2
            region.transitions.size shouldBe 1
        }
    }

    @Test
    fun `joins transitions to vertexes`() {
        assertSoftly {
            region.transitions.values.first().name shouldBe "transition"
            region.transitions.values.first().source.name shouldBe "INIT"
            region.transitions.values.first().target.name shouldBe "STATE_1"
        }
    }

    @Test
    fun `joins vertices to transitions`() {

        assertSoftly {
            region.vertices.getValue("INIT").incoming shouldHaveSize 0
            region.vertices.getValue("INIT").outgoing shouldHaveSize 1
            region.vertices.getValue("STATE_1").incoming shouldHaveSize 1
            region.vertices.getValue("STATE_1").outgoing shouldHaveSize 0
        }
    }

}