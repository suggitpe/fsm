package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.builders.FsmPrototypes

class FsmExecutionEnvironmentTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)

    @Test fun `handles simple events to transition to new state and records transitions`() {
        val executionEnvironment = createAStateMachineContextWithSimpleStates()
        executionEnvironment.handleEvent(aSimpleEventCalled("realEvent"))
        assertThat(theResultingState()).isEqualTo("FS")

        stateManager.printAudits()
    }

    private fun aSimpleEventCalled(eventName: String): BusinessEvent = BusinessEvent(eventName, BusinessObjectIdentifier("domain", "id", 0))
    private fun simpleStateMachinePrototype(): BehavioredClassifier = FsmPrototypes.simpleStateMachinePrototype().build()
    private fun createAStateMachineContextWithSimpleStates() = FsmExecutionEnvironment(simpleStateMachinePrototype(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState()
}