package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.FsmPrototypes.simpleNestedStatemachineProtoType
import org.suggs.fsm.stubs.BusinessEventStub
import org.suggs.fsm.stubs.BusinessEventStub.Companion.aBusinessEventCalled
import org.suggs.fsm.stubs.StubDomainObject
import org.suggs.fsm.stubs.StubFsmStateManager

class CompositeStateTransitionTest {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)
    private lateinit var executionEnvironment: FsmExecutionEnvironment
    private val domainObject = StubDomainObject(false)

    @BeforeEach fun `set up`() {
        executionEnvironment = createAStateMachineContextWithCompositeStates()
    }

    @Test fun `handles composite events to transition to new state and records transitions`() {
        executionEnvironment.handleEvent(aBusinessEventCalled("event1"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("region1::internalState")
    }

    @Test fun `handles composite events to transition to final state`() {
        domainObject.complete = false
        executionEnvironment.handleEvent(aBusinessEventCalled("event1"))
        assertThat(theResultingState()).endsWith("region1::internalState")

        domainObject.complete = true
        executionEnvironment.handleEvent(aBusinessEventCalled("event2"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("region0::final")
    }

    @Test fun `transitions away from composite states if no guard conditions`(){
        domainObject.complete = true
        executionEnvironment.handleEvent(aBusinessEventCalled("event1"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("region0::final")
    }

    private fun createAStateMachineContextWithCompositeStates() = FsmExecutionEnvironment(simpleNestedStatemachineProtoType{domainObject.areYouComplete()}.build(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState()
}