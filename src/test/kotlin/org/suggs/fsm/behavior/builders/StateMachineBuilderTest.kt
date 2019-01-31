package org.suggs.fsm.behavior.builders

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateMachineBuilder.Companion.aStateMachineCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled

class StateMachineBuilderTest {

    private val stateMachine = aStateMachineCalled("State Machine").withRegion(
            aRegionCalled("R0")
                    .withVertices(
                            anInitialPseudoStateCalled("INIT"),
                            aSimpleStateCalled("R0_S0"),
                            aSimpleStateCalled("R0_S1"),
                            aSimpleStateCalled("R0_S2"),
                            aFinalStateCalled("FINAL"))
                    .withTransitions(
                            anExternalTransitionCalled("R0_T0").startingAt("INIT").endingAt("R0_S0"),
                            anExternalTransitionCalled("R0_T1").startingAt("R0_S0").endingAt("R0_S1"),
                            anExternalTransitionCalled("R0_T2").startingAt("R0_S0").endingAt("R0_S2"),
                            anExternalTransitionCalled("R0_T3").startingAt("R0_S1").endingAt("FINAL"),
                            anExternalTransitionCalled("R0_T4").startingAt("R0_S2").endingAt("FINAL")
                    )).build()

    @Test
    fun `builds state machines`() {
        assertThat(stateMachine).isNotNull
    }
}