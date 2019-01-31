package org.suggs.fsm.behavior

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateBuilder.Companion.aStateCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled
import org.suggs.fsm.execution.*

class StateTest {

    private val simpleRegionWithOneStateContainingDeferableTriggers = aRegionCalled("testRegion").withVertices(
            aStateCalled("S0")
                    .withDeferrableTriggers(
                            aTriggerCalled("T1").firedWith(anEventCalled("event1")),
                            aTriggerCalled("T2").firedWith(anEventCalled("event2"))
                    ))
            .build()

    private val simpleRegionOfStatesAndTransitions = aRegionCalled("testRegion")
            .withVertices(
                    anInitialPseudoStateCalled("IS"),
                    aSimpleStateCalled("S0"),
                    aFinalStateCalled("FS")
            )
            .withTransitions(
                    anExternalTransitionCalled("T1").startingAt("IS").endingAt("S0"),
                    anExternalTransitionCalled("T2").startingAt("S0").endingAt("FS").withTriggers(
                            aTriggerCalled("T1").firedWith(anEventCalled("validEvent"))
                    )
            )
            .build()

    private val simpleRegionWithMultipleValidTransitionns = aRegionCalled("testRegion")
            .withVertices(aSimpleStateCalled("S1"), aSimpleStateCalled("S2"))
            .withTransitions(
                    anExternalTransitionCalled("T1").startingAt("S1").endingAt("S2").withTriggers(
                            aTriggerCalled("TG1").firedWith(anEventCalled("validEvent"))),
                    anExternalTransitionCalled("T2").startingAt("S1").endingAt("S2").withTriggers(
                            aTriggerCalled("TG2").firedWith(anEventCalled("validEvent")))
            ).build()

    private val namespaceContext = NamespaceObjectMapper()
    private val fsmExecutionContext = FsmExecutionContext(StubFsmStateManager())

    @BeforeEach fun init() {
        initMocks(this)
    }

    @Test fun `transitions to new state when valid transition`() {
        val state = simpleRegionOfStatesAndTransitions.findStateCalled("S0")
        fsmExecutionContext.stateManager.storeActiveState("S0")
        state.processEvent(aStubEventFor("validEvent"), fsmExecutionContext)
        assertThat(fsmExecutionContext.stateManager.getActiveState()).isEqualTo("FS")
    }

    @Test fun `does not transition to other states unless valid transition`() {
        val state = simpleRegionOfStatesAndTransitions.findStateCalled("S0")
        fsmExecutionContext.stateManager.storeActiveState("S0")
        state.processEvent(aStubEventFor("irrelevantEvent"), fsmExecutionContext)
        assertThat(fsmExecutionContext.stateManager.getActiveState()).isEqualTo("S0")
    }

    @Test fun `throws exception if it has more than one valid transaction`() {
        val state = simpleRegionWithMultipleValidTransitionns.findStateCalled("S1")
        assertThrows<RuntimeException> { state.processEvent(aStubEventFor("validEvent"), fsmExecutionContext) }
    }

    @Test
    fun `can identify deferable events`() {
        val state = simpleRegionWithOneStateContainingDeferableTriggers.findStateCalled("S0")
        assertThat(state.weCanDefer(aStubEventFor("event2"))).isTrue()
    }

    @Test
    fun `can identify non deferable events`() {
        val state = simpleRegionWithOneStateContainingDeferableTriggers.findStateCalled("S0")
        assertThat(state.weCanDefer(aStubEventFor("event3"))).isFalse()
    }

    private fun aStubEventFor(name: String): BusinessEvent {
        return BusinessEvent(name, BusinessObjectIdentifier("", "", 0))
    }
}