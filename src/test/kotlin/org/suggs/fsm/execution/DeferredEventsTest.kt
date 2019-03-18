package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.FinalState.Companion.DEFAULT_FINAL_STATE_NAME
import org.suggs.fsm.behavior.builders.FsmPrototypes.fsmWithDeferredAndAutomatedTransitionsPrototype
import org.suggs.fsm.behavior.builders.FsmPrototypes.fsmWithDeferredTransitionsPrototype
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

}