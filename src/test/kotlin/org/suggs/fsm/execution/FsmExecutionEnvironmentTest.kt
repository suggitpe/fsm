package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.builders.FsmPrototypes.simpleFsmPrototype
import org.suggs.fsm.stubs.BusinessEventStub
import org.suggs.fsm.stubs.BusinessEventStub.Companion.aBusinessEventCalled
import org.suggs.fsm.stubs.StubFsmStateManager
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.generateUmlFor
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.writePumlToFile

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
    private fun theResultingState() = stateManager.getActiveState()
}