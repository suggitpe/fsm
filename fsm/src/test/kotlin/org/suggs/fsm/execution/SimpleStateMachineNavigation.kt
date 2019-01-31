package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.builders.FsmPrototypes

class SimpleStateMachineNavigation {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)
    private lateinit var executionEnvironment: FsmExecutionEnvironment

    @BeforeEach fun `set up execution environment`() {
        executionEnvironment = createAStateMachineContextWithSimpleRouting()
    }

    @Test fun `does not transition if the events do not fire triggers`() {
        assertThrows<UnprocessableEventException> { executionEnvironment.handleEvent(aSimpleEventCalled("goNorth")) }
        assertThat(theResultingState()).isEqualTo("Middle")

        stateManager.printAudits()
    }

    @Test fun `transitions from existing states`() {
        stateManager.storeActiveState("Left")
        executionEnvironment.handleEvent(aSimpleEventCalled("finish"))
        assertThat(theResultingState()).isEqualTo("End")

        stateManager.printAudits()
    }

    @Test fun `transitions different directions depending on the event type - goLeft`() {
        executionEnvironment.handleEvent(aSimpleEventCalled("goLeft"))
        assertThat(theResultingState()).isEqualTo("Left")

        executionEnvironment.handleEvent(aSimpleEventCalled("finish"))
        assertThat(theResultingState()).isEqualTo("End")

        stateManager.printAudits()
    }

    @Test fun `transitions different directions depending on the event type - goRight`() {
        executionEnvironment.handleEvent(aSimpleEventCalled("goRight"))
        assertThat(theResultingState()).isEqualTo("Right")

        executionEnvironment.handleEvent(aSimpleEventCalled("finish"))
        assertThat(theResultingState()).isEqualTo("End")

        stateManager.printAudits()
    }

    private fun aSimpleEventCalled(eventName: String): BusinessEvent = BusinessEvent(eventName, BusinessObjectIdentifier("domain", "id", 0))
    private fun simpleStateMachineWithTwoOutcomesPrototype(): BehavioredClassifier = FsmPrototypes.simpleStateMachineWithTwoOutcomesPrototype().build()
    private fun createAStateMachineContextWithSimpleRouting() = FsmExecutionEnvironment(simpleStateMachineWithTwoOutcomesPrototype(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState()
}