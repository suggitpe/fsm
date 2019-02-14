package org.suggs.fsm.execution

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.FsmPrototypes.nestedStateStateMachinePrototype
import org.suggs.fsm.stubs.BusinessEventStub
import org.suggs.fsm.stubs.StubFsmStateManager

class CompositeStateTransitionTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)

    @Test fun `handles composite events to transition to new state and records transitions`() {
        val executionEnvironment = createAStateMachineContextWithCompositeStates()
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("succeeded"))
        Assertions.assertThat(theResultingState()).endsWith("final")

        stateManager.printAudits()
    }


    private fun createAStateMachineContextWithCompositeStates() = FsmExecutionEnvironment(nestedStateStateMachinePrototype().build(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState()
}