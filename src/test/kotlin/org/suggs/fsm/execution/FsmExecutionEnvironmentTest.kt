package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.builders.FsmPrototypes.simpleFsmPrototype
import org.suggs.fsm.uml.StateMachineUmlGenerator
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.generateUmlFor
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.writePumlToFile

class FsmExecutionEnvironmentTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)

    @Test
    fun `describes simple state machines`() {
        writePumlToFile(generateUmlFor(simpleFsmPrototype().build()), "simpleFsm.puml")
    }

    @Test fun `handles simple events to transition to new state and records transitions`() {
        val executionEnvironment = createAStateMachineContextWithSimpleStates()
        executionEnvironment.handleEvent(aSimpleEventCalled("realEvent"))
        assertThat(theResultingState()).isEqualTo("Final")

        stateManager.printAudits()
    }

    private fun aSimpleEventCalled(eventName: String): BusinessEvent = BusinessEvent(eventName, BusinessObjectIdentifier("domain", "id", 0))
    private fun simpleStateMachine(): BehavioredClassifier = simpleFsmPrototype().build()
    private fun createAStateMachineContextWithSimpleStates() = FsmExecutionEnvironment(simpleStateMachine(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState()
}