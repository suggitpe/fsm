package org.suggs.fsm.execution

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.builders.FsmPrototypes.fsmWithDeferredAutomatedTransitionsPrototype

class DeferredEventsTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)

    @Test fun `handles simple events to transition to new state and records transitions`() {
        val executionEnvironment = createAStateMachineContextWithDeferredEvents()
        executionEnvironment.handleEvent(aSimpleEventCalled("realEvent"))
        Assertions.assertThat(theResultingState()).isEqualTo("FS")

        stateManager.printAudits()
    }

    private fun aSimpleEventCalled(eventName: String): BusinessEvent = BusinessEvent(eventName, BusinessObjectIdentifier("domain", "id", 0))
    private fun deferredStatesStateMachine(): BehavioredClassifier = fsmWithDeferredAutomatedTransitionsPrototype().build()
    private fun createAStateMachineContextWithDeferredEvents() = FsmExecutionEnvironment(deferredStatesStateMachine(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState()
}