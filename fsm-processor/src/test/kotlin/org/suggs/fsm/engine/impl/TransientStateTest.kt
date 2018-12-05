package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.slf4j.LoggerFactory
import org.slf4j.LoggerFactory.getLogger
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.suggs.fsm.FsmEventInterceptorStub
import org.suggs.fsm.bo.BusinessObjectIdentifier
import org.suggs.fsm.engine.uml2.behaviorstatemachines.UnprocessableEventException
import org.suggs.fsm.event.IEvent
import org.suggs.fsm.framework.api.IStateMachine
import org.suggs.fsm.framework.api.IStateMachineFactory
import org.suggs.fsm.framework.spi.IActionExecutor
import org.suggs.fsm.framework.spi.IFsmEventInterceptor
import org.suggs.fsm.framework.spi.IStateManager
import org.suggs.fsm.uml2.scribe.runtime.IEventContext
import java.util.*

class TransientStateTest {

    private val log = getLogger(TransientStateTest::class.java)

    private lateinit var stateMachineFactory: IStateMachineFactory
    private lateinit var stateMachine: IStateMachine
    private lateinit var testStateManager: StateManager
    private lateinit var eventInterceptor: IFsmEventInterceptor

    @BeforeEach fun `set up`() {
        eventInterceptor = FsmEventInterceptorStub()
        val ctx = AnnotationConfigApplicationContext(StateMachineFactoryExecutionConfig::class.java)
        stateMachineFactory = ctx.getBean("stateMachineFactory") as IStateMachineFactory
        stateMachineFactory.setFsmEventInterceptor(eventInterceptor)
        stateMachine = stateMachineFactory.getStateMachine("testTransientInternalEvent")
        assertThat(stateMachine).isNotNull

        // Set the state manager
        testStateManager = StateManager()
    }

    @Test fun `transitions when events received`() {

        val mockEvent = createMockEventWith("top::context::e1")

        // TODO: should not use exceptions for flow control
        try {
            sendEvent(stateMachine, mockEvent, testStateManager, HashMap())
        } catch (e: UnprocessableEventException) {
            log.warn("Silly people using exceptions for flow control ${e.message}")
        }
        assertThat(testStateManager.activeState).isEqualTo("top::context::State Machine::region0::R0_S1")
        log.debug("--> Current state is: " + testStateManager.activeState)
    }

    /**
     * creates a state machine with one transient state at the start, and two other states. An entry action on
     * the transient state should trigger a transition to one of the other two states automatically.
     */
    @Test fun `transitions when internal events are fired`() {

        stateMachine.namespaceObjectManager.addObject("myEntryAction", createEntryActionThatFiresEventNamed("top::context::internalEvent1"))

        val mockEvent = createMockEventWith("top::context::e1")

        // TODO: should not use exceptions for flow control
        try {
            // should trigger an internal event and go to state R0_S2
            sendEvent(stateMachine, mockEvent, testStateManager, HashMap())
        } catch (e: UnprocessableEventException) {
            log.warn("Silly people using exceptions for flow control ${e.message}")
        }

        assertThat(testStateManager.activeState).isEqualTo("top::context::State Machine::region0::R0_S2")

        log.debug("--> Current state is: " + testStateManager.activeState)
    }

    private fun createEntryActionThatFiresEventNamed(eventName: String): IActionExecutor {
        return object : IActionExecutor {
            internal var name: String = ""

            override fun executeAction(eventContext: IEventContext) {
                log.debug("Executing executeAction()")
                eventContext.internalEvent = eventName
                log.debug("executeAction() - internal event set.")
            }

            override fun getName(): String {
                return name
            }

            override fun setName(name: String) {
                this.name = name
            }
        }
    }

    /**
     * Creates a mock event with a specified event type.
     */
    private fun createMockEventWith(eventType: String): IEvent {
        val event = mock(IEvent::class.java)
        `when`(event.type).thenReturn(eventType)
        `when`(event.businessObjectIdentifier).thenReturn(BusinessObjectIdentifier("", "", "", "", 0))
        `when`(event.businessObject).thenReturn(null)
        return event
    }

    /**
     * Sends an event to a stateful trade confirmation and prints the elapsed processing time to System.out.
     */
    private fun sendEvent(eventHandler: IStateMachine, event: IEvent, stateManager: IStateManager?, context: Map<String, String>) {
        val startTime = System.currentTimeMillis()
        eventHandler.handleEvent(event, stateManager, context)
        val elapsedTime = System.currentTimeMillis() - startTime
        log.debug("Processed event ${event.type} in $elapsedTime ms.")
    }

    /**
     * Basic state manager implementation.
     */
    inner class StateManager : IStateManager {

        private var activeState: String? = null
        private var historyStates: Map<String, String> = HashMap()
        private var deferredEvents: List<IEventContext> = ArrayList()

        override fun storeActiveState(activeState: String) {
            this.activeState = activeState
        }

        override fun getActiveState(): String? {
            return activeState
        }

        override fun getHistoryStates(): Map<String, String> {
            return historyStates
        }

        override fun storeHistoryStates(historyStateMap: Map<String, String>) {
            historyStates = historyStateMap
            log.debug("Storing history states: $historyStateMap")
        }

        override fun storeDeferredEvents(eventList: List<IEventContext>) {
            deferredEvents = eventList
            log.debug("Storing deferred events: $deferredEvents")
        }

        override fun getDeferredEvents(): List<IEventContext> {
            return deferredEvents
        }
    }
}