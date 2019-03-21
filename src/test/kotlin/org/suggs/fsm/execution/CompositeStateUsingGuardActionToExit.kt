package org.suggs.fsm.execution

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.*
import org.suggs.fsm.stubs.BusinessEventStub
import org.suggs.fsm.stubs.BusinessEventStub.Companion.aBusinessEventCalled
import org.suggs.fsm.stubs.BusinessObjectReferenceStub
import org.suggs.fsm.stubs.StubDomainObject
import org.suggs.fsm.stubs.StubFsmStateManager

class CompositeStateUsingGuardActionToExit {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)
    private lateinit var executionEnvironment: FsmExecutionEnvironment
    private val domainObject = StubDomainObject(false)

    @BeforeEach fun `set up`() {
        executionEnvironment = createANestedStateStateMachineWithGuardExit()
    }

    @Test fun `transitions to internal processing state from initial event`() {
        executionEnvironment.handleEvent(BusinessEventStub.aBusinessEventCalled("event1"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("region1::processing")
    }

    @Test fun `remains in processing state when guard condition not met`() {
        executionEnvironment.handleEvent(aBusinessEventCalled("event1"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("region1::processing")
    }

    @Test fun `remains in processing state until guard condition is met`() {
        executionEnvironment.handleEvent(aBusinessEventCalled("event1"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        domainObject.complete = true
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))

        stateManager.printAudits()

        assertThat(theResultingState()).endsWith("region0::FINAL")
    }

    private fun createANestedStateStateMachineWithGuardExit() = FsmExecutionEnvironment(nestedStateStateMachineWithGuardExitPrototype { domainObject.areYouComplete() }.build(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState(BusinessObjectReferenceStub.aBOReferenceForTest())

    companion object {
        fun nestedStateStateMachineWithGuardExitPrototype(finalTransitionGuard: (BusinessEvent) -> Boolean) =
                StateMachineBuilder.aStateMachineCalled("context").withRegion(
                        RegionBuilder.aRegionCalled("region0")
                                .withVertices(
                                        VertexBuilder.anInitialPseudoState(),
                                        VertexBuilder.aSimpleStateCalled("state1"),
                                        VertexBuilder.aCompositeStateCalled("state2").withRegion(
                                                RegionBuilder.aRegionCalled("region1")
                                                        .withVertices(
                                                                VertexBuilder.anInitialPseudoState(),
                                                                VertexBuilder.aSimpleStateCalled("processing")
                                                        )
                                                        .withTransitions(
                                                                TransitionBuilder.aTransitionCalled("trans1").startingAtInitialState().endingAt("processing"),
                                                                TransitionBuilder.aTransitionCalled("trans2").startingAt("processing").endingAt("processing").triggeredBy(EventBuilder.anEventCalled("process"))
                                                        )
                                        ),
                                        VertexBuilder.aFinalState()
                                )
                                .withTransitions(
                                        TransitionBuilder.aTransitionCalled("trans1").startingAtInitialState().endingAt("state1"),
                                        TransitionBuilder.aTransitionCalled("trans2").startingAt("state1").endingAt("state2").triggeredBy(EventBuilder.anEventCalled("event1")),
                                        TransitionBuilder.aTransitionCalled("trans3").startingAt("state2").endingAtFinalState().guardedBy(ConstraintBuilder.aConstraintCalled("completed").withGuardCondition(finalTransitionGuard))
                                )
                )

    }
}