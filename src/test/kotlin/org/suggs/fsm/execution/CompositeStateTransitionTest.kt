package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.FsmPrototypes.nestedStateStateMachinePrototype
import org.suggs.fsm.stubs.BusinessEventStub
import org.suggs.fsm.stubs.StubFsmStateManager

class CompositeStateTransitionTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)
    private lateinit var executionEnvironment: FsmExecutionEnvironment

    @BeforeEach fun `set up`() {
        executionEnvironment = createAStateMachineContextWithCompositeStates()
    }

    @Disabled("Not implemented")
    @Test fun `handles composite events to transition to new state and records transitions`() {
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("succeeded"))
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("succeeded"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("final")
    }

    @Test fun `handles abort event to final`() {
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("aborted"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("final")
    }

    @Test fun `handles series of events to aborted`(){
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("succeeded"))
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("aborted"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("final")
    }

    private fun createAStateMachineContextWithCompositeStates() = FsmExecutionEnvironment(nestedStateStateMachinePrototype().build(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState()
}