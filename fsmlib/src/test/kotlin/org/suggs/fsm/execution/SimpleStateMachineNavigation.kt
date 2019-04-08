package org.suggs.fsm.execution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateMachineBuilder.Companion.aStateMachineCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.stubs.BusinessObjectReferenceStub.Companion.aBOReferenceForTest
import org.suggs.fsm.stubs.StubFsmStateManager

class SimpleStateMachineNavigation {

    private val stateManager: StubFsmStateManager = StubFsmStateManager()
    private val fsmExecutionContext: FsmExecutionContext = FsmExecutionContext(stateManager)
    private lateinit var executionEnvironment: FsmExecutionEnvironment

    @BeforeEach fun `set up execution environment`() {
        executionEnvironment = createAStateMachineContextWithSimpleRouting()
    }

    @Test fun `does not transition if the events do not fire triggers`() {
        assertThrows<UnprocessableEventException> { executionEnvironment.handleEvent(aSimpleEventCalled("goNorth")) }
        assertThat(theResultingState()).endsWith("middle")

        stateManager.printAudits()
    }

    @Test fun `transitions from existing states`() {
        stateManager.storeActiveState("context::region0::left")
        executionEnvironment.handleEvent(aSimpleEventCalled("finish"))
        assertThat(theResultingState()).endsWith("end")

        stateManager.printAudits()
    }

    @Test fun `transitions different directions depending on the event type - goLeft`() {
        executionEnvironment.handleEvent(aSimpleEventCalled("goLeft"))
        assertThat(theResultingState()).endsWith("left")

        executionEnvironment.handleEvent(aSimpleEventCalled("finish"))
        assertThat(theResultingState()).endsWith("end")

        stateManager.printAudits()
    }

    @Test fun `transitions different directions depending on the event type - goRight`() {
        executionEnvironment.handleEvent(aSimpleEventCalled("goRight"))
        assertThat(theResultingState()).endsWith("right")

        executionEnvironment.handleEvent(aSimpleEventCalled("finish"))
        assertThat(theResultingState()).endsWith("end")

        stateManager.printAudits()
    }

    private fun aSimpleEventCalled(eventName: String): BusinessEvent = BusinessEvent(eventName, BusinessObjectReference("domain", "id", 0))
    private fun createAStateMachineContextWithSimpleRouting() = FsmExecutionEnvironment(fsmWithTwoOutcomesPrototype().build(), fsmExecutionContext)
    private fun theResultingState() = stateManager.getActiveState(aBOReferenceForTest())

    companion object {
        fun fsmWithTwoOutcomesPrototype() =
                aStateMachineCalled("context").withRegion(
                        aRegionCalled("region0")
                                .withVertices(
                                        anInitialPseudoStateCalled("start"),
                                        aSimpleStateCalled("middle"),
                                        aSimpleStateCalled("left"),
                                        aSimpleStateCalled("right"),
                                        aFinalStateCalled("end"))
                                .withTransitions(
                                        aTransitionCalled("_trigger1").startingAt("start").endingAt("middle"),
                                        aTransitionCalled("_trigger2").startingAt("middle").endingAt("left").triggeredBy(anEventCalled("goLeft")),
                                        aTransitionCalled("_trigger3").startingAt("middle").endingAt("right").triggeredBy(anEventCalled("goRight")),
                                        aTransitionCalled("_trigger4").startingAt("left").endingAt("end").triggeredBy(anEventCalled("finish")),
                                        aTransitionCalled("_trigger5").startingAt("right").endingAt("end").triggeredBy(anEventCalled("finish"))
                                )
                )
    }
}