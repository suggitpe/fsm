package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.FinalState.Companion.DEFAULT_FINAL_STATE_NAME
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateMachineBuilder.Companion.aStateMachineCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalState
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoState
import org.suggs.fsm.stubs.BusinessEventStub.Companion.aBusinessEventCalled
import org.suggs.fsm.stubs.BusinessObjectReferenceStub.Companion.aBOReferenceForTest
import org.suggs.fsm.stubs.StubFsmStateManager

class DeferredEventsTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)

    @Test fun `fires deferred events automatically`() {
        val executionEnvironment = createAStateMachineContextWithDeferredEvents()
        sendEventsToDeferIn(executionEnvironment)
        assertThat(stateManager.deferredEvents.size == 2)

        executionEnvironment.handleEvent(aBusinessEventCalled("realEvent"))

        stateManager.printAudits()
        assertThat(theResultingState()).endsWith(DEFAULT_FINAL_STATE_NAME)
    }

    @Test fun `transitions automatic transitions over deferred events`() {
        val executionEnvironment = createAStatemachineWithDeferredAndAutomaticTransitions()
        sendEventsToDeferIn(executionEnvironment)
        assertThat(stateManager.deferredEvents.size == 2)

        executionEnvironment.handleEvent(aBusinessEventCalled("realEvent"))

        stateManager.printAudits()
        assertThat(theResultingState()).endsWith("state3")
    }

    private fun sendEventsToDeferIn(executionEnvironment: FsmExecutionEnvironment) {
        executionEnvironment.handleEvent(aBusinessEventCalled("deferredEvent1"))
        executionEnvironment.handleEvent(aBusinessEventCalled("deferredEvent2"))
    }

    private fun theResultingState() = stateManager.getActiveState(aBOReferenceForTest())
    private fun createAStateMachineContextWithDeferredEvents() = FsmExecutionEnvironment(fsmWithDeferredTransitionsPrototype().build(), fsmExecutionContext)
    private fun createAStatemachineWithDeferredAndAutomaticTransitions() = FsmExecutionEnvironment(fsmWithDeferredAndAutomatedTransitionsPrototype().build(), fsmExecutionContext)

    companion object {
        fun fsmWithDeferredTransitionsPrototype() =
                aStateMachineCalled("context").withRegion(
                        aRegionCalled("region0")
                                .withVertices(
                                        anInitialPseudoState(),
                                        aSimpleStateCalled("state1")
                                                .withDeferrableTriggers(
                                                        aTriggerCalled("State1_DE1").firedWith(anEventCalled("deferredEvent1")),
                                                        aTriggerCalled("State1_DE3").firedWith(anEventCalled("deferredEvent2"))),
                                        aSimpleStateCalled("state2")
                                                .withDeferrableTriggers(
                                                        aTriggerCalled("State2_DE1").firedWith(anEventCalled("deferredEvent1")),
                                                        aTriggerCalled("State2_DE3").firedWith(anEventCalled("deferredEvent2"))),
                                        aSimpleStateCalled("state3")
                                                .withDeferrableTriggers(
                                                        aTriggerCalled("State3_DE1").firedWith(anEventCalled("deferredEvent2"))),
                                        aSimpleStateCalled("state4"),
                                        aFinalState()
                                )
                                .withTransitions(
                                        aTransitionCalled("region0_trigger1").startingAtInitialState().endingAt("state1"),
                                        aTransitionCalled("region0_trigger2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("realEvent")),
                                        aTransitionCalled("region0_trigger3").startingAt("state2").endingAt("state3"),
                                        aTransitionCalled("region0_trigger4").startingAt("state3").endingAt("state4").triggeredBy(anEventCalled("deferredEvent1")),
                                        aTransitionCalled("region0_trigger5").startingAt("state4").endingAtFinalState().triggeredBy(anEventCalled("deferredEvent2"))
                                )
                )

        fun fsmWithDeferredAndAutomatedTransitionsPrototype() =
                aStateMachineCalled("context").withRegion(
                        aRegionCalled("region0")
                                .withVertices(
                                        anInitialPseudoState(),
                                        aSimpleStateCalled("state1")
                                                .withDeferrableTriggers(
                                                        aTriggerCalled("S1_DT1").firedWith(anEventCalled("deferredEvent1")),
                                                        aTriggerCalled("S1_DT2").firedWith(anEventCalled("deferredEvent2"))),
                                        aSimpleStateCalled("state2")
                                                .withDeferrableTriggers(
                                                        aTriggerCalled("S2_DT1").firedWith(anEventCalled("deferredEvent1")),
                                                        aTriggerCalled("S2_DT2").firedWith(anEventCalled("deferredEvent2"))
                                                ),
                                        aSimpleStateCalled("state3"),
                                        aSimpleStateCalled("state4"),
                                        aSimpleStateCalled("state5"),
                                        aFinalState()
                                )
                                .withTransitions(
                                        aTransitionCalled("T1").startingAtInitialState().endingAt("state1"),
                                        aTransitionCalled("T2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("realEvent")),
                                        aTransitionCalled("T3").startingAt("state2").endingAt("state3").triggeredBy(anEventCalled("deferredEvent1")),
                                        aTransitionCalled("T4").startingAt("state2").endingAt("state4"),
                                        aTransitionCalled("T5").startingAt("state2").endingAt("state5").triggeredBy(anEventCalled("deferredEvent2")),
                                        aTransitionCalled("T6").startingAt("state4").endingAt("state3").triggeredBy(anEventCalled("deferredEvent1")),
                                        aTransitionCalled("T7").startingAt("state4").endingAt("state5").triggeredBy(anEventCalled("deferredEvent2")),
                                        aTransitionCalled("T8").startingAt("state4").endingAtFinalState().triggeredBy(anEventCalled("otherEvent")),
                                        aTransitionCalled("T9").startingAt("state5").endingAtFinalState().triggeredBy(anEventCalled("deferredEvent1"))
                                )
                )

    }

}