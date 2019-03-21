package org.suggs.fsm.behavior

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.behavior.TransitionKind.EXTERNAL
import org.suggs.fsm.behavior.TransitionKind.INTERNAL
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.BusinessObjectReference
import org.suggs.fsm.execution.FsmExecutionContext
import org.suggs.fsm.stubs.RegionContainerStub.Companion.aRegionContainerStub
import org.suggs.fsm.stubs.StubFsmStateManager

class TransitionTest {

    private lateinit var testRegion: Region

    @Mock private lateinit var sourceState: State
    @Mock private lateinit var targetState: State
    private var event = aBusinessEventCalled("testEvent")
    private var fsmStateManager = StubFsmStateManager()
    private var fsmExecutionContext = FsmExecutionContext(fsmStateManager)

    @BeforeEach fun `initialise region`() {
        initMocks(this)
        testRegion = aRegionCalled("region")
                .withVertices(
                        aSimpleStateCalled("state1"),
                        aSimpleStateCalled("state2")
                )
                .withTransitions(
                        aTransitionCalled("transition").startingAt("state1").endingAt("state2").triggeredBy(
                                aTriggerCalled("trigger").firedWith(anEventCalled("event"))
                        )
                )
                .build(aRegionContainerStub())
    }

    @Test fun `external transitions fire exit and entry behaviours`() {
        val externalTransition = Transition("T1", EXTERNAL, sourceState, targetState)
        externalTransition.fire(event, fsmExecutionContext)
        verify(sourceState, times(1)).doExitAction(event, fsmExecutionContext)
        verify(targetState, times(1)).doEntryAction(event, fsmExecutionContext)
    }

    @Test fun `internal transitions do not fire exit and entry behaviors`() {
        val internalTransition = Transition("T1", INTERNAL, sourceState, targetState)
        internalTransition.fire(event, fsmExecutionContext)
        verify(sourceState, never()).doExitAction(event, fsmExecutionContext)
        verify(targetState, never()).doEntryAction(event, fsmExecutionContext)
    }

    @Test fun `can tell you if they are fireable for an event`() {
        val transition = testRegion.findTransitionCalled("transition")
        assertThat(transition.isFireableFor(aBusinessEventCalled("event"))).isTrue()
    }

    @Test fun `can tell you if they are not firable for an event`() {
        val transition = testRegion.findTransitionCalled("transition")
        assertThat(transition.isFireableFor(aBusinessEventCalled("irrelevantEvent"))).isFalse()
    }

    private fun aBusinessEventCalled(name: String): BusinessEvent {
        return BusinessEvent(name, BusinessObjectReference("", "", 0))
    }
}