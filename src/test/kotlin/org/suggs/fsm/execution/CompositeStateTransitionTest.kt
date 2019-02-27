package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.FinalState.Companion.DEFAULT_FINAL_STATE_NAME
import org.suggs.fsm.behavior.builders.FsmPrototypes.simpleNestedStatemachineProtoType
import org.suggs.fsm.stubs.BusinessEventStub
import org.suggs.fsm.stubs.StubFsmStateManager

class CompositeStateTransitionTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)
    private lateinit var executionEnvironment: FsmExecutionEnvironment

    @BeforeEach fun `set up`() {
        executionEnvironment = createAStateMachineContextWithCompositeStates()
    }

    @Test fun `handles composite events to transition to new state and records transitions`() {
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("event1"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("region1::internalState")
    }

    @Disabled
    @Test fun `handles composite events to transition to final state`() {
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("event1"))
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("event2"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("region0::final")
    }

   private fun createAStateMachineContextWithCompositeStates() = FsmExecutionEnvironment(simpleNestedStatemachineProtoType().build(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState()
}