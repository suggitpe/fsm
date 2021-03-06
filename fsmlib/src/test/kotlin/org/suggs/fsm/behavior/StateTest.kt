package org.suggs.fsm.behavior

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.SimpleStateBuilder.Companion.aStateCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.BusinessObjectReference
import org.suggs.fsm.execution.FsmExecutionContext
import org.suggs.fsm.execution.UnprocessableEventException
import org.suggs.fsm.stubs.RegionContainerStub.Companion.aRegionContainerStub
import org.suggs.fsm.stubs.StubFsmStateManager

class StateTest {

    private val fsmExecutionContext = FsmExecutionContext(StubFsmStateManager())

    @Test fun `transitions to new state when valid transition`() {
        val state = simpleRegionOfStatesAndTransitions.findStateCalled("S0")
        fsmExecutionContext.stateManager.storeActiveState("S0")
        val event = aStubEventFor("validEvent")

        state.processEvent(event, fsmExecutionContext)

        assertThat(fsmExecutionContext.stateManager.getActiveState(event.identifier)).endsWith("FS")
    }

    @Test fun `does not transition to other states unless valid transition`() {
        val state = simpleRegionOfStatesAndTransitions.findStateCalled("S0")
        fsmExecutionContext.stateManager.storeActiveState("S0")
        val event = aStubEventFor("irrelevantEvent")

        assertThatExceptionOfType(UnprocessableEventException::class.java).isThrownBy {
            state.processEvent(event, fsmExecutionContext)
        }

        assertThat(fsmExecutionContext.stateManager.getActiveState(event.identifier)).isEqualTo("S0")
    }

    @Test fun `throws exception if it has more than one valid transaction`() {
        val state = simpleRegionWithMultipleValidTransitionns.findStateCalled("S1")
        assertThatExceptionOfType(RuntimeException::class.java).isThrownBy {
            state.processEvent(aStubEventFor("validEvent"), fsmExecutionContext)
        }
    }

    @Test fun `can identify deferable events`() {
        val state = simpleRegionWithOneStateContainingDeferableTriggers.findStateCalled("S0")
        assertThat(state.weCanDefer(aStubEventFor("event2"))).isTrue()
    }

    @Test fun `can identify non deferable events`() {
        val state = simpleRegionWithOneStateContainingDeferableTriggers.findStateCalled("S0")
        assertThat(state.weCanDefer(aStubEventFor("event3"))).isFalse()
    }

    @Test fun `can find triggeringEvents from a list of events`() {
        val state = simpleRegionOfStatesAndTransitions.findStateCalled("S0")
        val events = state.findEventsThatFireTransitionsFrom(setOf(Event("somethingRandom"), Event("validEvent"), Event("foo"), Event("bar")))
        assertThat(events).isEqualTo(setOf(Event("validEvent")))
    }

    @Test fun `finds all triggeringEvents from a list of events`() {
        val state = simpleRegionOfStatesAndTransitions.findStateCalled("S0")
        val events = state.findEventsThatFireTransitionsFrom(setOf(Event("anotherValidEvent"), Event("validEvent"), Event("foo"), Event("bar")))
        assertThat(events).isEqualTo(setOf(Event("anotherValidEvent"), Event("validEvent")))
    }

    @Test fun `returns null when no events match the `() {
        val state = simpleRegionOfStatesAndTransitions.findStateCalled("S0")
        assertThat(state.findEventsThatFireTransitionsFrom(setOf(Event("foo"), Event("bar"), Event("baz")))).isEmpty()
    }

    private fun aStubEventFor(name: String): BusinessEvent {
        return BusinessEvent(name, BusinessObjectReference("", "", 0))
    }

    private val simpleRegionWithOneStateContainingDeferableTriggers = aRegionCalled("testRegion").withVertices(
            aStateCalled("S0")
                    .withDeferrableTriggers(
                            aTriggerCalled("T1").firedWith(anEventCalled("event1")),
                            aTriggerCalled("T2").firedWith(anEventCalled("event2"))
                    ))
            .build(aRegionContainerStub())

    private val simpleRegionOfStatesAndTransitions = aRegionCalled("testRegion")
            .withVertices(
                    anInitialPseudoStateCalled("IS"),
                    aSimpleStateCalled("S0"),
                    aFinalStateCalled("FS")
            )
            .withTransitions(
                    aTransitionCalled("T1").startingAt("IS").endingAt("S0"),
                    aTransitionCalled("T2").startingAt("S0").endingAt("FS").triggeredBy(
                            aTriggerCalled("TG1").firedWith(anEventCalled("validEvent")),
                            aTriggerCalled("TG2").firedWith(anEventCalled("anotherValidEvent"))
                    )
            )
            .build(aRegionContainerStub())

    private val simpleRegionWithMultipleValidTransitionns = aRegionCalled("testRegion")
            .withVertices(aSimpleStateCalled("S1"), aSimpleStateCalled("S2"))
            .withTransitions(
                    aTransitionCalled("T1").startingAt("S1").endingAt("S2").triggeredBy(
                            aTriggerCalled("TG1").firedWith(anEventCalled("validEvent"))),
                    aTransitionCalled("T2").startingAt("S1").endingAt("S2").triggeredBy(
                            aTriggerCalled("TG2").firedWith(anEventCalled("validEvent")))
            ).build(aRegionContainerStub())
}