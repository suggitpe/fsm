package org.suggs.fsm.behavior

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.stubs.RegionContainerStub.Companion.aRegionContainerStub

class RegionTest {

    @Test
    fun `regions have initial states`() {
        val region = aRegionCalled("foo").withVertices(
            anInitialPseudoStateCalled("initial"),
            aSimpleStateCalled("state"),
            aFinalStateCalled("final")
        ).build(aRegionContainerStub())

        region.getInitialState().name shouldBe "initial"
    }

    @Test
    fun `throws exceptions when regions have no initial state`() {
        val region = aRegionCalled("foo").withVertices(
            aSimpleStateCalled("state"),
            aFinalStateCalled("final")
        ).build(aRegionContainerStub())

        shouldThrow<IllegalStateException> {
            region.getInitialState()
        }
    }
}