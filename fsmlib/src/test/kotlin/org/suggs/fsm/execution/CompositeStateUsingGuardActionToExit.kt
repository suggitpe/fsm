package org.suggs.fsm.execution

import io.kotest.matchers.string.shouldEndWith
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.ConstraintBuilder.Companion.aConstraintCalled
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateMachineBuilder.Companion.aStateMachineCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aCompositeStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalState
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoState
import org.suggs.fsm.stubs.BusinessEventStub.Companion.aBusinessEventCalled
import org.suggs.fsm.stubs.BusinessObjectReferenceStub.Companion.aBOReferenceForTest
import org.suggs.fsm.stubs.StubDomainObject
import org.suggs.fsm.stubs.StubFsmStateManager

class CompositeStateUsingGuardActionToExit {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)
    private lateinit var executionEnvironment: FsmExecutionEnvironment
    private val domainObject = StubDomainObject(false)

    @BeforeEach
    fun `set up`() {
        executionEnvironment = createANestedStateStateMachineWithGuardExit()
    }

    @Test
    fun `transitions to internal processing state from initial event`() {
        executionEnvironment.handleEvent(aBusinessEventCalled("event1"))

        stateManager.printAudits()

        theResultingState() shouldEndWith "region1::processing"
    }

    @Test
    fun `remains in processing state when guard condition not met`() {
        executionEnvironment.handleEvent(aBusinessEventCalled("event1"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))

        stateManager.printAudits()

        theResultingState() shouldEndWith "region1::processing"
    }

    @Test
    fun `remains in processing state until guard condition is met`() {
        executionEnvironment.handleEvent(aBusinessEventCalled("event1"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))
        domainObject.complete = true
        executionEnvironment.handleEvent(aBusinessEventCalled("process"))

        stateManager.printAudits()

        theResultingState() shouldEndWith "region0::FINAL"
    }

    private fun createANestedStateStateMachineWithGuardExit() = FsmExecutionEnvironment(nestedStateStateMachineWithGuardExitPrototype { domainObject.areYouComplete() }.build(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState(aBOReferenceForTest())

    companion object {
        fun nestedStateStateMachineWithGuardExitPrototype(finalTransitionGuard: (BusinessEvent) -> Boolean) =
            aStateMachineCalled("context").withRegion(
                aRegionCalled("region0")
                    .withVertices(
                        anInitialPseudoState(),
                        aSimpleStateCalled("state1"),
                        aCompositeStateCalled("state2").withRegion(
                            aRegionCalled("region1")
                                .withVertices(
                                    anInitialPseudoState(),
                                    aSimpleStateCalled("processing")
                                )
                                .withTransitions(
                                    aTransitionCalled("trans1").startingAtInitialState().endingAt("processing"),
                                    aTransitionCalled("trans2").startingAt("processing").endingAt("processing").triggeredBy(anEventCalled("process"))
                                )
                        ),
                        aFinalState()
                    )
                    .withTransitions(
                        aTransitionCalled("trans1").startingAtInitialState().endingAt("state1"),
                        aTransitionCalled("trans2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("event1")),
                        aTransitionCalled("trans3").startingAt("state2").endingAtFinalState().guardedBy(aConstraintCalled("completed").withGuardCondition(finalTransitionGuard))
                    )
            )

    }
}