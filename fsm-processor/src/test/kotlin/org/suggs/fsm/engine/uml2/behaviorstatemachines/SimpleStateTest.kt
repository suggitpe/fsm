package org.suggs.fsm.engine.uml2.behaviorstatemachines

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.event.IEvent
import org.suggs.fsm.event.IEventFactory
import org.suggs.fsm.framework.spi.IStateManager
import org.suggs.fsm.uml2.basicbehaviors.IBehavior
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion
import org.suggs.fsm.uml2.behaviorstatemachines.IState
import org.suggs.fsm.uml2.behaviorstatemachines.IState.TRANSITIONING
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition
import org.suggs.fsm.uml2.communications.ITrigger
import org.suggs.fsm.uml2.scribe.runtime.IEventContext
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext
import org.suggs.fsm.uml2.scribe.runtime.IStateEntryListener
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext
import java.util.*

class SimpleStateTest {

    private val STATE_NAME = "testState"
    private lateinit var state: IState

    @Mock lateinit var region: IRegion
    @Mock lateinit var enclosingState: IState
    @Mock lateinit var incomingEvent: IEvent
    @Mock lateinit var triggerEvent: org.suggs.fsm.uml2.communications.IEvent
    @Mock lateinit var completionEvent: IEvent
    @Mock lateinit var eventContext: IEventContext
    @Mock lateinit var transition: ITransition
    @Mock lateinit var stateMachineContext: IStateMachineContext
    @Mock lateinit var namespaceContext: INamespaceContext
    @Mock lateinit var stateManager: IStateManager
    @Mock lateinit var stateEntryListener: IStateEntryListener
    @Mock lateinit var transitionTrigger: ITrigger
    @Mock lateinit var entryBehavior: IBehavior
    @Mock lateinit var exitBehavior: IBehavior
    @Mock lateinit var eventFactory: IEventFactory

    @BeforeEach fun `set uo`() {
        initMocks(this)
        state = State("SIMPLE")
    }

    @Test
    fun `accessors stupid test`() {
        state.container = region
        assertThat(state.container).isEqualTo(region)

        state.entryBehavior = entryBehavior
        assertThat(state.entryBehavior).isEqualTo(entryBehavior)

        state.exitBehavior = exitBehavior
        assertThat(state.exitBehavior).isEqualTo(exitBehavior)
    }

    /**
     * Test that a GenrealRuntimeException is thrown if adding a `null` state entry listener.
     */
    @Test fun `null stateEntryListener throws runtime exception`() {
        assertThrows<RuntimeException> { state.addStateEntryListener(false, null) }
    }

    /**
     * Tests that the exit behavior is called when the state is exited.
     */
    @Test fun `exit behavior called on doExit`() {
        state.exitBehavior = exitBehavior
        // Exit the state
        state.doExitAction(eventContext, namespaceContext, stateMachineContext)
        verify(exitBehavior).execute(eventContext, namespaceContext, stateMachineContext)
    }

    /**
     * Tests that the entry behavior is called when the state is entered.
     */
    @Test fun `entry behavior called on doExit`() {
        state.entryBehavior = entryBehavior
        // Enter the state
        state.doEntryAction(eventContext, namespaceContext, stateMachineContext)
        verify<IBehavior>(entryBehavior).execute(eventContext, namespaceContext, stateMachineContext)
    }

    /**
     * Tests that the current state for the state machine is set to   IState.TRANSITIONING on exit..
     */
    @Test fun `state set to transitioning state on exit`() {
        state.name = STATE_NAME
        `when`(stateMachineContext.stateManager).thenReturn(stateManager)
        // Exit the state
        state.exit(eventContext, namespaceContext, stateMachineContext)
        verify(stateManager).storeActiveState(TRANSITIONING)
    }

    /**
     * Tests that the following occur on state entry: 1) The state is set as the current state. 2) Outgoing transitions are
     * identified and fired. 3) The state entry listener registered with the state is notified.
     */
    @Test fun `state entry`() {

        val qualifiedEventName = "	eventName"

        state.name = STATE_NAME

        `when`(enclosingState.allPossibleOutgoingTransitions).thenReturn(ArrayList())
        `when`(region.state).thenReturn(enclosingState)
        `when`(incomingEvent.type).thenReturn(qualifiedEventName)
        `when`(incomingEvent.businessObject).thenReturn(null)
        `when`(eventContext.event).thenReturn(incomingEvent)

        `when`(triggerEvent.qualifiedName).thenReturn(qualifiedEventName)
        `when`(stateMachineContext.stateManager).thenReturn(stateManager)
        `when`(stateMachineContext.eventFactory).thenReturn(eventFactory)
        `when`(eventFactory.createEvent(org.suggs.fsm.uml2.communications.IEvent.COMPLETION_EVENT_QUALIFIED_NAME, incomingEvent)).thenReturn(completionEvent)
        `when`(transitionTrigger.event).thenReturn(triggerEvent)
        `when`(transition.triggers).thenReturn(listOf(transitionTrigger))

        `when`<Boolean>(transition.isEnabled(eventContext, namespaceContext, stateMachineContext)).thenReturn(true)

        stateEntryListener.stateEntered(state, stateMachineContext)

        // Set up the state
        state.addStateEntryListener(false, stateEntryListener)
        state.addOutgoingTransition(transition)

        state.container = region

        `when`(stateManager.deferredEvents).thenReturn(listOf())

        // Enter the state
        state.enter(eventContext, namespaceContext, stateMachineContext)

        verify(stateManager).storeActiveState(state.qualifiedName)
    }
}