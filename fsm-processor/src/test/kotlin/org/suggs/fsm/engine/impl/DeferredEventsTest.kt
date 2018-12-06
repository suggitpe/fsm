package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.slf4j.LoggerFactory.getLogger
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.suggs.fsm.FsmEventInterceptorStub
import org.suggs.fsm.MockStateManager
import org.suggs.fsm.bo.BusinessObjectIdentifier
import org.suggs.fsm.event.IEvent
import org.suggs.fsm.framework.api.IStateMachine
import org.suggs.fsm.framework.api.IStateMachineFactory
import org.suggs.fsm.framework.spi.IFsmEventInterceptor
import org.suggs.fsm.framework.spi.IStateManager
import java.util.*

class DeferredEventsTest {

    private val log = getLogger(DeferredEventsTest::class.java)

    private lateinit var stateMachineFactory: IStateMachineFactory
    private lateinit var stateMachine: IStateMachine
    private lateinit var testStateManager: IStateManager
    private lateinit var eventInterceptor: IFsmEventInterceptor

    @BeforeEach fun `set up`() {
        eventInterceptor = FsmEventInterceptorStub()

        val ctx = AnnotationConfigApplicationContext(StateMachineFactoryExecutionConfig::class.java)
        stateMachineFactory = ctx.getBean("stateMachineFactory") as IStateMachineFactory

        stateMachineFactory.setFsmEventInterceptor(eventInterceptor)

        // Set the state manager
        testStateManager = MockStateManager()
    }

    /**
     * tests that deferred events correctly trigger transitions automatically on entering states where they are fireable.
     */
    @Test fun `automatic transitions`() {
        // Build the state machine
        stateMachine = stateMachineFactory.createStateMachine("testAutomaticTrans")
        assertThat(stateMachine).isNotNull

        val e1 = createMockEventFor("top::context::e1")
        val e2 = createMockEventFor("top::context::e2")
        val e3 = createMockEventFor("top::context::e3")
        val e10 = createMockEventFor("top::context::e10")

        sendEvent(stateMachine, e1, testStateManager, HashMap()) // deferred
        sendEvent(stateMachine, e2, testStateManager, HashMap()) // deferred
        sendEvent(stateMachine, e3, testStateManager, HashMap()) // deferred

        assertThat(testStateManager.deferredEvents.size).isEqualTo(3)

        // should trigger a series of transitions thru till the last
        // state.
        sendEvent(stateMachine, e10, testStateManager, HashMap())

        assertThat(testStateManager.activeState).isEqualTo("top::context::State Machine::region0::R0_FS")

        log.debug("Current state is: ${testStateManager.activeState}")
    }

    /**
     * tests that automatic transitions take priority over deferred events, and also, that deferred events
     * are checked in the order they were deferred.
     */
    @Test fun `defer queue`() {
        // Build the state machine
        stateMachine = stateMachineFactory.createStateMachine("testDeferQueue")
        assertThat(stateMachine).isNotNull

        val e1 = createMockEventFor("top::context::e1")
        val e2 = createMockEventFor("top::context::e2")
        val e10 = createMockEventFor("top::context::e10")

        sendEvent(stateMachine, e1, testStateManager, HashMap()) // deferred
        sendEvent(stateMachine, e2, testStateManager, HashMap()) // deferred

        assertThat(testStateManager.deferredEvents.size).isEqualTo(2)

        // should trigger a series of state changes, as transitions
        // are automatically taken due to the deferred events firing.
        sendEvent(stateMachine, e10, testStateManager, HashMap())

        assertThat(testStateManager.activeState).isEqualTo("top::context::State Machine::region0::R0_S3")

        log.debug("Current state is: ${testStateManager.activeState}")
    }

    /**
     * tests that discarding of deferred events works correctly.
     */
    @Test fun `defer discard`() {
        // Build the state machine
        stateMachine = stateMachineFactory.createStateMachine("testDeferDiscard")
        assertThat(stateMachine).isNotNull

        val e1 = createMockEventFor("top::context::e1")
        val e2 = createMockEventFor("top::context::e2")
        val e3 = createMockEventFor("top::context::e3")
        val e10 = createMockEventFor("top::context::e10")

        sendEvent(stateMachine, e1, testStateManager, HashMap()) // deferred
        sendEvent(stateMachine, e2, testStateManager, HashMap()) // deferred
        sendEvent(stateMachine, e3, testStateManager, HashMap()) // deferred

        assertThat(testStateManager.deferredEvents.size).isEqualTo(3)

        // should trigger a series of state changes, as transitions
        // are automatically taken due to the deferred events firing.
        sendEvent(stateMachine, e10, testStateManager, HashMap())

        assertThat(testStateManager.activeState).isEqualTo("top::context::State Machine::region0::R0_S4")

        assertThat(testStateManager.deferredEvents.size).isEqualTo(0)

        log.debug("Current state is: ${testStateManager.activeState}")
    }

    /**
     * Creates a mock event with a specified event type.
     */
    private fun createMockEventFor(eventType: String): IEvent {
        val event = mock(IEvent::class.java)
        `when`(event.type).thenReturn(eventType)
        `when`(event.businessObjectIdentifier).thenReturn(BusinessObjectIdentifier("", "", "", "", 0))
        `when`(event.businessObject).thenReturn(null)
        return event
    }

    /**
     * Sends an event to a stateful trade confirmation and prints the
     * elapsed processing time to System.out.
     */
    private fun sendEvent(eventHandler: IStateMachine, event: IEvent, stateManager: IStateManager?, context: Map<String, String>) {
        val startTime = System.currentTimeMillis()
        eventHandler.handleEvent(event, stateManager, context)
        val elapsedTime = System.currentTimeMillis() - startTime
        log.debug("Processed event ${event.type} in $elapsedTime  ms.")
    }
}