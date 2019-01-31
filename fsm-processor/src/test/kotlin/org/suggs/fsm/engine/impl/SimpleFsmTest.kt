package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.suggs.fsm.FsmEventInterceptorStub
import org.suggs.fsm.MockStateManager
import org.suggs.fsm.engine.uml2.behaviorstatemachines.UnprocessableEventException
import org.suggs.fsm.event.BaseEventFactory
import org.suggs.fsm.event.IEvent
import org.suggs.fsm.framework.api.IStateMachine
import org.suggs.fsm.framework.api.IStateMachineFactory
import org.suggs.fsm.framework.spi.IFsmEventInterceptor
import org.suggs.fsm.framework.spi.IStateManager
import java.util.*

class SimpleFsmTest {

    private val log = LoggerFactory.getLogger(DeferredEventsTest::class.java)

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

    @Test fun `state machines transition through to FS if they can`() {
        stateMachine = stateMachineFactory.createStateMachine("simpleStateMachine")
        Assertions.assertThat(stateMachine).isNotNull

        sendEvent(stateMachine, createEventFor("realEvent"), testStateManager, HashMap())

        log.debug("State machine now in state ${testStateManager.activeState}")
        assertThat(testStateManager.activeState).endsWith("region0::R0_FS")
    }

    @Test fun `does not process events that are unexpected`() {
        stateMachine = stateMachineFactory.createStateMachine("simpleStateMachine")
        Assertions.assertThat(stateMachine).isNotNull

        assertThrows<UnprocessableEventException> {
            sendEvent(stateMachine, createEventFor("nonEvent"), testStateManager, HashMap())
        }

        log.debug("State machine now in state ${testStateManager.activeState}")
        assertThat(testStateManager.activeState).endsWith("region0::R0_S1")
    }

    @Test fun `COMPLETION events auto-transition to FS`() {
        stateMachine = stateMachineFactory.createStateMachine("simpleStateMachine")
        Assertions.assertThat(stateMachine).isNotNull
        testStateManager.storeActiveState("top::context::State Machine::region0::R0_S1")

        sendEvent(stateMachine, createEventFor("realEvent"), testStateManager, HashMap())

        log.debug("State machine now in state ${testStateManager.activeState}")
        assertThat(testStateManager.activeState).endsWith("region0::R0_FS")
    }

    @Test fun `state machines transition from IS for all events`() {
        stateMachine = stateMachineFactory.createStateMachine("simpleStateMachine")
        assertThat(stateMachine).isNotNull

        try {
            sendEvent(stateMachine, createEventFor("nonEvent"), testStateManager, HashMap())
        }
        catch(e: UnprocessableEventException){
            // this is fine
        }
        log.debug("State machine now in state ${testStateManager.activeState}")
        assertThat(testStateManager.activeState).endsWith("region0::R0_S1")
    }

    private fun createEventFor(eventType: String): IEvent {
        return BaseEventFactory().createEvent(eventType, mock(IEvent::class.java))
    }

    private fun sendEvent(eventHandler: IStateMachine, event: IEvent, stateManager: IStateManager?, context: Map<String, String>) {
        eventHandler.handleEvent(event, stateManager, context)
    }
}