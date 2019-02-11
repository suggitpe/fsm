package org.suggs.fsm.behavior

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.stubs.NamespaceStub.Companion.aNamespaceStub

class RegionTest {


    @Test
    fun `regions have initial states`() {
                val region = aRegionCalled("foo").withVertices(
                anInitialPseudoStateCalled("initial"),
                aSimpleStateCalled("state"),
                aFinalStateCalled("final")
        ).build(aNamespaceStub())

        assertThat(region.getInitialState().name).isEqualTo("initial")
    }

    @Test
    fun `throws exceptions when regions have no initial state`() {
        val region = aRegionCalled("foo").withVertices(
                aSimpleStateCalled("state"),
                aFinalStateCalled("final")
        ).build(aNamespaceStub())

        assertThrows<IllegalStateException>{region.getInitialState()}
    }
}