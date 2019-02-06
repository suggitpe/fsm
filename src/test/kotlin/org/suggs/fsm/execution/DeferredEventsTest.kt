package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.FsmPrototypes.fsmWithDeferredAndAutomatedTransitionsPrototype
import org.suggs.fsm.behavior.builders.FsmPrototypes.fsmWithDeferredTransitionsPrototype
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.generateUmlFor
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.writePumlToFile

class DeferredEventsTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)

    @Test
    fun `describes state machines with deferred events`() {
        writePumlToFile(generateUmlFor(fsmWithDeferredTransitionsPrototype().build()), "deferredTransitions.puml")
    }

    @Test
    fun `describes state machine with deferred and automated transitions`() {
        writePumlToFile(generateUmlFor(fsmWithDeferredAndAutomatedTransitionsPrototype().build()), "deferredAndAutomatedTransitions.puml")
    }

    @Test fun `fires deferred events automatically`() {
        val executionEnvironment = createAStateMachineContextWithDeferredEvents()
        sendEventsToDeferIn(executionEnvironment)
        assertThat(stateManager.deferredEvents.size == 2)

        executionEnvironment.handleEvent(aSimpleEventCalled("realEvent"))

        stateManager.printAudits()
        assertThat(theResultingState()).isEqualTo("Final")
    }

    @Test fun `transitions automatic transitions over deferred events`() {
        val executionEnvironment = createAStatemachineWithDeferredAndAutomaticTransitions()
        sendEventsToDeferIn(executionEnvironment)
        assertThat(stateManager.deferredEvents.size == 2)

        executionEnvironment.handleEvent(aSimpleEventCalled("realEvent"))

        stateManager.printAudits()
        assertThat(theResultingState()).isEqualTo("State3")
    }

    private fun sendEventsToDeferIn(executionEnvironment: FsmExecutionEnvironment) {
        executionEnvironment.handleEvent(aSimpleEventCalled("deferredEvent1"))
        executionEnvironment.handleEvent(aSimpleEventCalled("deferredEvent2"))
    }

    private fun aSimpleEventCalled(eventName: String): BusinessEvent = BusinessEvent(eventName, BusinessObjectIdentifier("domain", "id", 0))
    private fun theResultingState() = stateManager.getActiveState()

    private fun fsmWithDeferredTransitions() = fsmWithDeferredTransitionsPrototype().build()
    private fun fsmWithDeferredAndAutomatedTransitions() = fsmWithDeferredAndAutomatedTransitionsPrototype().build()

    private fun createAStateMachineContextWithDeferredEvents() = FsmExecutionEnvironment(fsmWithDeferredTransitions(), fsmExecutionContext)
    private fun createAStatemachineWithDeferredAndAutomaticTransitions() = FsmExecutionEnvironment(fsmWithDeferredAndAutomatedTransitions(), fsmExecutionContext)

}