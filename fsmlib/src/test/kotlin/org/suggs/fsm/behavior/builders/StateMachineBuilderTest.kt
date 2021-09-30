package org.suggs.fsm.behavior.builders

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateMachineBuilder.Companion.aStateMachineCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoState

class StateMachineBuilderTest {

    private val stateMachine = aStateMachineCalled("State Machine").withRegion(
        aRegionCalled("R0")
            .withVertices(
                anInitialPseudoState(),
                aSimpleStateCalled("R0_S0"),
                aSimpleStateCalled("R0_S1"),
                aSimpleStateCalled("R0_S2"),
                aFinalStateCalled("FINAL")
            )
            .withTransitions(
                aTransitionCalled("R0_T0").startingAtInitialState().endingAt("R0_S0"),
                aTransitionCalled("R0_T1").startingAt("R0_S0").endingAt("R0_S1"),
                aTransitionCalled("R0_T2").startingAt("R0_S0").endingAt("R0_S2"),
                aTransitionCalled("R0_T3").startingAt("R0_S1").endingAt("FINAL"),
                aTransitionCalled("R0_T4").startingAt("R0_S2").endingAt("FINAL")
            )
    ).build()

    @Test
    fun `builds state machines`() {
        stateMachine shouldNotBe null
    }
}