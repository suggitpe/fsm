package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateMachineBuilder.Companion.aStateMachineCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalState
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoState
import org.suggs.fsm.stubs.BusinessEventStub.Companion.aBusinessEventCalled
import org.suggs.fsm.stubs.BusinessObjectReferenceStub.Companion.aBOReferenceForTest
import org.suggs.fsm.stubs.StubFsmStateManager

class FsmExecutionEnvironmentTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)

    @Test fun `handles simple events to transition to new state and records transitions`() {
        val executionEnvironment = createAStateMachineContextWithSimpleStates()
        executionEnvironment.handleEvent(aBusinessEventCalled("realEvent"))
        assertThat(theResultingState()).endsWith("FINAL")

        stateManager.printAudits()
    }

    private fun createAStateMachineContextWithSimpleStates() = FsmExecutionEnvironment(simpleFsmPrototype().build(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState(aBOReferenceForTest())

    companion object {
        fun simpleFsmPrototype() =
                aStateMachineCalled("context").withRegion(
                        aRegionCalled("region0")
                                .withVertices(
                                        anInitialPseudoState(),
                                        aSimpleStateCalled("state1"),
                                        aSimpleStateCalled("state2"),
                                        aFinalState())
                                .withTransitions(
                                        aTransitionCalled("_trigger1").startingAtInitialState().endingAt("state1"),
                                        aTransitionCalled("_trigger2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("realEvent")),
                                        aTransitionCalled("_trigger3").startingAt("state2").endingAtFinalState()
                                )
                )
    }
}