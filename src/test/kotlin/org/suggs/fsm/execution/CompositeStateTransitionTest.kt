package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateMachineBuilder.Companion.aStateMachineCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aCompositeStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.stubs.BusinessEventStub.Companion.aBusinessEventCalled
import org.suggs.fsm.stubs.BusinessObjectReferenceStub.Companion.aBOReferenceForTest
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

    @Test fun `transitions away from composite states if no guard conditions`() {
        domainObject.complete = true
        executionEnvironment.handleEvent(aBusinessEventCalled("event1"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("region0::final")
    }

    private fun createAStateMachineContextWithCompositeStates() = FsmExecutionEnvironment(simpleNestedStatemachineProtoType { domainObject.areYouComplete() }.build(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState(aBOReferenceForTest())

    companion object {
        fun simpleNestedStatemachineProtoType(finalTransitionGuard: (BusinessEvent) -> Boolean) =
                aStateMachineCalled("context").withRegion(
                        aRegionCalled("region0")
                                .withVertices(
                                        anInitialPseudoStateCalled("initial"),
                                        aSimpleStateCalled("simpleState"),
                                        aCompositeStateCalled("compositeState").withRegion(
                                                aRegionCalled("region1")
                                                        .withVertices(
                                                                anInitialPseudoStateCalled("initial"),
                                                                aSimpleStateCalled("internalState"),
                                                                aFinalStateCalled("final")
                                                        )
                                                        .withTransitions(
                                                                aTransitionCalled("transition1").startingAt("initial").endingAt("internalState"),
                                                                aTransitionCalled("transition2").startingAt("internalState").endingAt("final").triggeredBy(anEventCalled("event2"))
                                                        )
                                        ),
                                        aFinalStateCalled("final")
                                )
                                .withTransitions(
                                        aTransitionCalled("transition1").startingAt("initial").endingAt("simpleState"),
                                        aTransitionCalled("transition2").startingAt("simpleState").endingAt("compositeState").triggeredBy(anEventCalled("event1")),
                                        aTransitionCalled("transition3").startingAt("compositeState").endingAt("final").guardedBy(finalTransitionGuard)
                                )
                )
    }
}