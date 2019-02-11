package org.suggs.fsm.behavior

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateBuilder.Companion.aStateCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.execution.*
import org.suggs.fsm.stubs.NamespaceStub
import org.suggs.fsm.stubs.NamespaceStub.Companion.aNamespaceStub
import org.suggs.fsm.stubs.StubFsmStateManager

class StateTest {

    private val fsmExecutionContext = FsmExecutionContext(StubFsmStateManager())

    @Test fun `transitions to new state when valid transition`() {
        val state = simpleRegionOfStatesAndTransitions.findStateCalled("S0")
        fsmExecutionContext.stateManager.storeActiveState("S0")
        state.processEvent(aStubEventFor("validEvent"), fsmExecutionContext)
        assertThat(fsmExecutionContext.stateManager.getActiveState()).endsWith("FS")
    }

    @Test fun `does not transition to other states unless valid transition`() {
        val state = simpleRegionOfStatesAndTransitions.findStateCalled("S0")
        fsmExecutionContext.stateManager.storeActiveState("S0")
        assertThrows<UnprocessableEventException> { state.processEvent(aStubEventFor("irrelevantEvent"), fsmExecutionContext) }
        assertThat(fsmExecutionContext.stateManager.getActiveState()).isEqualTo("S0")
    }

    @Test fun `throws exception if it has more than one valid transaction`() {
        val state = simpleRegionWithMultipleValidTransitionns.findStateCalled("S1")
        assertThrows<RuntimeException> { state.processEvent(aStubEventFor("validEvent"), fsmExecutionContext) }
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
        return BusinessEvent(name, BusinessObjectIdentifier("", "", 0))
    }

    private val simpleRegionWithOneStateContainingDeferableTriggers = aRegionCalled("testRegion").withVertices(
            aStateCalled("S0")
                    .withDeferrableTriggers(
                            aTriggerCalled("T1").firedWith(anEventCalled("event1")),
                            aTriggerCalled("T2").firedWith(anEventCalled("event2"))
                    ))
            .build(aNamespaceStub())

    private val simpleRegionOfStatesAndTransitions = aRegionCalled("testRegion")
            .withVertices(
                    anInitialPseudoStateCalled("IS"),
                    aSimpleStateCalled("S0"),
                    aFinalStateCalled("FS")
            )
            .withTransitions(
                    anExternalTransitionCalled("T1").startingAt("IS").endingAt("S0"),
                    anExternalTransitionCalled("T2").startingAt("S0").endingAt("FS").triggeredBy(
                            aTriggerCalled("TG1").firedWith(anEventCalled("validEvent")),
                            aTriggerCalled("TG2").firedWith(anEventCalled("anotherValidEvent"))
                    )
            )
            .build(aNamespaceStub())

    private val simpleRegionWithMultipleValidTransitionns = aRegionCalled("testRegion")
            .withVertices(aSimpleStateCalled("S1"), aSimpleStateCalled("S2"))
            .withTransitions(
                    anExternalTransitionCalled("T1").startingAt("S1").endingAt("S2").triggeredBy(
                            aTriggerCalled("TG1").firedWith(anEventCalled("validEvent"))),
                    anExternalTransitionCalled("T2").startingAt("S1").endingAt("S2").triggeredBy(
                            aTriggerCalled("TG2").firedWith(anEventCalled("validEvent")))
            ).build(aNamespaceStub())
}