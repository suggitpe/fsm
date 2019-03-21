package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.FinalState.Companion.DEFAULT_FINAL_STATE_NAME
import org.suggs.fsm.behavior.builders.*
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
                StateMachineBuilder.aStateMachineCalled("context").withRegion(
                        RegionBuilder.aRegionCalled("region0")
                                .withVertices(
                                        VertexBuilder.anInitialPseudoState(),
                                        VertexBuilder.aSimpleStateCalled("state1")
                                                .withDeferrableTriggers(
                                                        TriggerBuilder.aTriggerCalled("State1_DE1").firedWith(EventBuilder.anEventCalled("deferredEvent1")),
                                                        TriggerBuilder.aTriggerCalled("State1_DE3").firedWith(EventBuilder.anEventCalled("deferredEvent2"))),
                                        VertexBuilder.aSimpleStateCalled("state2")
                                                .withDeferrableTriggers(
                                                        TriggerBuilder.aTriggerCalled("State2_DE1").firedWith(EventBuilder.anEventCalled("deferredEvent1")),
                                                        TriggerBuilder.aTriggerCalled("State2_DE3").firedWith(EventBuilder.anEventCalled("deferredEvent2"))),
                                        VertexBuilder.aSimpleStateCalled("state3")
                                                .withDeferrableTriggers(
                                                        TriggerBuilder.aTriggerCalled("State3_DE1").firedWith(EventBuilder.anEventCalled("deferredEvent2"))),
                                        VertexBuilder.aSimpleStateCalled("state4"),
                                        VertexBuilder.aFinalState()
                                )
                                .withTransitions(
                                        TransitionBuilder.aTransitionCalled("region0_trigger1").startingAtInitialState().endingAt("state1"),
                                        TransitionBuilder.aTransitionCalled("region0_trigger2").startingAt("state1").endingAt("state2").triggeredBy(EventBuilder.anEventCalled("realEvent")),
                                        TransitionBuilder.aTransitionCalled("region0_trigger3").startingAt("state2").endingAt("state3"),
                                        TransitionBuilder.aTransitionCalled("region0_trigger4").startingAt("state3").endingAt("state4").triggeredBy(EventBuilder.anEventCalled("deferredEvent1")),
                                        TransitionBuilder.aTransitionCalled("region0_trigger5").startingAt("state4").endingAtFinalState().triggeredBy(EventBuilder.anEventCalled("deferredEvent2"))
                                )
                )

        fun fsmWithDeferredAndAutomatedTransitionsPrototype() =
                StateMachineBuilder.aStateMachineCalled("context").withRegion(
                        RegionBuilder.aRegionCalled("region0")
                                .withVertices(
                                        VertexBuilder.anInitialPseudoState(),
                                        VertexBuilder.aSimpleStateCalled("state1")
                                                .withDeferrableTriggers(
                                                        TriggerBuilder.aTriggerCalled("S1_DT1").firedWith(EventBuilder.anEventCalled("deferredEvent1")),
                                                        TriggerBuilder.aTriggerCalled("S1_DT2").firedWith(EventBuilder.anEventCalled("deferredEvent2"))),
                                        VertexBuilder.aSimpleStateCalled("state2")
                                                .withDeferrableTriggers(
                                                        TriggerBuilder.aTriggerCalled("S2_DT1").firedWith(EventBuilder.anEventCalled("deferredEvent1")),
                                                        TriggerBuilder.aTriggerCalled("S2_DT2").firedWith(EventBuilder.anEventCalled("deferredEvent2"))
                                                ),
                                        VertexBuilder.aSimpleStateCalled("state3"),
                                        VertexBuilder.aSimpleStateCalled("state4"),
                                        VertexBuilder.aSimpleStateCalled("state5"),
                                        VertexBuilder.aFinalState()
                                )
                                .withTransitions(
                                        TransitionBuilder.aTransitionCalled("T1").startingAtInitialState().endingAt("state1"),
                                        TransitionBuilder.aTransitionCalled("T2").startingAt("state1").endingAt("state2").triggeredBy(EventBuilder.anEventCalled("realEvent")),
                                        TransitionBuilder.aTransitionCalled("T3").startingAt("state2").endingAt("state3").triggeredBy(EventBuilder.anEventCalled("deferredEvent1")),
                                        TransitionBuilder.aTransitionCalled("T4").startingAt("state2").endingAt("state4"),
                                        TransitionBuilder.aTransitionCalled("T5").startingAt("state2").endingAt("state5").triggeredBy(EventBuilder.anEventCalled("deferredEvent2")),
                                        TransitionBuilder.aTransitionCalled("T6").startingAt("state4").endingAt("state3").triggeredBy(EventBuilder.anEventCalled("deferredEvent1")),
                                        TransitionBuilder.aTransitionCalled("T7").startingAt("state4").endingAt("state5").triggeredBy(EventBuilder.anEventCalled("deferredEvent2")),
                                        TransitionBuilder.aTransitionCalled("T8").startingAt("state4").endingAtFinalState().triggeredBy(EventBuilder.anEventCalled("otherEvent")),
                                        TransitionBuilder.aTransitionCalled("T9").startingAt("state5").endingAtFinalState().triggeredBy(EventBuilder.anEventCalled("deferredEvent1"))
                                )
                )

    }

}