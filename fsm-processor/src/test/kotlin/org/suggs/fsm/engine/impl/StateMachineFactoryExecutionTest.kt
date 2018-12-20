package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.slf4j.LoggerFactory.getLogger
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.suggs.fsm.FsmEventInterceptorStub
import org.suggs.fsm.MockActionExecutor
import org.suggs.fsm.MockGuardEvaluator
import org.suggs.fsm.MockStateManager
import org.suggs.fsm.bo.BusinessObjectIdentifier
import org.suggs.fsm.engine.uml2.behaviorstatemachines.UnprocessableEventException
import org.suggs.fsm.event.IEvent
import org.suggs.fsm.framework.api.IStateMachine
import org.suggs.fsm.framework.api.IStateMachineFactory
import org.suggs.fsm.framework.spi.IStateManager
import org.suggs.fsm.uml2.behaviorstatemachines.IState
import org.suggs.fsm.uml2.communications.ITrigger
import java.util.*

class StateMachineFactoryExecutionTest {

    private val log = getLogger(StateMachineFactoryExecutionTest::class.java)

    private lateinit var stateMachine: IStateMachine
    private lateinit var stateManager: IStateManager

    @BeforeEach fun setUp() {
        val eventInterceptor = FsmEventInterceptorStub()

        val ctx = AnnotationConfigApplicationContext(StateMachineFactoryExecutionConfig::class.java)
        val stateMachineFactory = ctx.getBean("stateMachineFactory") as IStateMachineFactory
        stateMachineFactory.setFsmEventInterceptor(eventInterceptor)

        // Build the state machine
        stateMachine = stateMachineFactory.createStateMachine("TestFsm")
        assertThat(stateMachine).isNotNull

        // Set the state manager
        stateManager = MockStateManager()

        // Don't listen to events or exceptions
        //stateMachine.setFsmEventInterceptor(null);
    }

    /**
     * Binds guards and actions to the state machine and sends events.
     */
    @Test fun testStraightRunWithRuntimeBinding() {
        bindGuardsAndActions()
        sendEvents(stateMachine)
        assertThat(stateManager.activeState).isEqualTo("top::context::State Machine::region0::R0_FS")
        log.debug("Current state is: ${stateManager.activeState}")
    }

    /**
     * Sends events to the state machine, using the guards and actions
     * specified in the state machine definition.
     */
    @Test fun testStraightRunWithStateMachineDefinitionBinding() {
        sendEvents(stateMachine)
        assertThat(stateManager.activeState).isEqualTo("top::context::State Machine::region0::R0_FS")
        log.debug("Current state is: ${stateManager.activeState}")
    }

    /**
     * The state machine defined in TestFsm.xml defines deferrable
     * events on some of the states. This tests whether a sub-state
     * which has parent states with deferrable events defined on them,
     * correctly inherits those deferrable events.
     */
    @Test fun testDeferredEventInheritance() {
        // setup some dummy objects
        val e10 = createMockEventFor("top::context::e10")
        val e11 = createMockEventFor("top::context::e11")
        val e5 = createMockEventFor("top::context::e5")

        // test start
        val omgr = stateMachine.namespaceObjectManager

        // get state R2_S1 from the state machine
        val r2_s1 = omgr.getObject("top::context::State Machine::region0::R0_S1::region1::R1_S2::region2::R2_S1") as IState

        // R2_S1 should have 3 deferrable triggers, 1 defined on itself,
        // and 2 inherited from its parents R1_S2 and R0_S1 ( R2_S1 is inside
        // R1_S2, which in turn is inside R0_S1).
        var deferredTriggers: Set<ITrigger> = r2_s1.deferrableTriggers

        assertThat(deferredTriggers.size).isEqualTo(3)
        assertThat(r2_s1.defersEvent(e10)).isTrue() // from R2_S1
        assertThat(r2_s1.defersEvent(e11)).isTrue() // from R1_S2
        assertThat(r2_s1.defersEvent(e5)).isTrue() // from RO_S1

        // ---------------------------------------------------------------------
        // Test another state.
        // get state R2_S2, this should only have 2 deferred events in
        // its list
        // ---------------------------------------------------------------------
        val r2_s2 = omgr.getObject("top::context::State Machine::region0::R0_S1::region1::R1_S2::region2::R2_S2") as IState

        // R2_S2 should have 2 deferrable triggers - the 2 inherited
        // from
        // its parents R1_S2 and R0_S1
        deferredTriggers = r2_s2.deferrableTriggers

        assertThat(deferredTriggers.size).isEqualTo(2)
        assertThat(r2_s2.defersEvent(e11)).isTrue() // from R1_S2
        assertThat(r2_s2.defersEvent(e5)).isTrue() // from RO_S1

        // ---------------------------------------------------------------------
        // Test another state.
        // get state R1_S5, this should only have 1 deferred event in
        // its list
        // ---------------------------------------------------------------------
        val r1_s5 = omgr.getObject("top::context::State Machine::region0::R0_S1::region1::R1_S5") as IState

        // R1_S5 should have 1 deferrable trigger - the one inherited
        // from
        // its parent R0_S1
        deferredTriggers = r1_s5.deferrableTriggers

        assertThat(deferredTriggers.size).isEqualTo(1)
        assertThat(r1_s5.defersEvent(e5)).isTrue() // from RO_S1

        // ---------------------------------------------------------------------
        // Finally, this state should have nothing in its deferred
        // event list,
        // as it has no enclosing states, and nothing defined on
        // itself
        // ---------------------------------------------------------------------
        val r0_s3 = omgr.getObject("top::context::State Machine::region0::R0_S3") as IState

        // R0_S3 should have nothing
        deferredTriggers = r0_s3.deferrableTriggers

        assertThat(deferredTriggers.size).isEqualTo(0)
    }

    /**
     * Creates and binds guards and actions to the state machine,
     * replacing any guard or action implementations specified in the
     * state machine definition.
     */
    private fun bindGuardsAndActions() {
        // Create and bind guards and actions
        val g1 = MockGuardEvaluator("g1")
        val g2 = MockGuardEvaluator("g2")

        val a1 = MockActionExecutor("a1")
        val a2 = MockActionExecutor("a2")
        val a3 = MockActionExecutor("a3")
        val a4 = MockActionExecutor("a4")
        val a5 = MockActionExecutor("a5")
        val a6 = MockActionExecutor("a6")

        // Bind the guards
        stateMachine.namespaceObjectManager.addObject("g1", g1)
        stateMachine.namespaceObjectManager.addObject("g2", g2)

        // Bind the actions
        stateMachine.namespaceObjectManager.addObject("a1", a1)
        stateMachine.namespaceObjectManager.addObject("a2", a2)
        stateMachine.namespaceObjectManager.addObject("a3", a3)
        stateMachine.namespaceObjectManager.addObject("a4", a4)
        stateMachine.namespaceObjectManager.addObject("a5", a5)
        stateMachine.namespaceObjectManager.addObject("a6", a6)
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
     * Sends event to an event handler in a pre-defined script
     */
    private fun sendEvents(stateMachineDefinition: IStateMachine) {

        val e1 = createMockEventFor("top::context::e1")
        val e2 = createMockEventFor("top::context::e2")
        val e3 = createMockEventFor("top::context::e3")
        val e4 = createMockEventFor("top::context::e4")
        val e5 = createMockEventFor("top::context::e5")
        val e6 = createMockEventFor("top::context::e6")
        val e7 = createMockEventFor("top::context::e7")
        val e9 = createMockEventFor("top::context::e9")
        val e10 = createMockEventFor("top::context::e10")
        val e11 = createMockEventFor("top::context::e11")

        sendEvent(stateMachineDefinition, e1, stateManager, HashMap())
        sendEvent(stateMachineDefinition, e10, stateManager, HashMap()) // deferred
        sendEvent(stateMachineDefinition, e11, stateManager, HashMap()) // deferred
        sendEvent(stateMachineDefinition, e5, stateManager, HashMap()) // deferred
        sendEvent(stateMachineDefinition, e7, stateManager, HashMap())
        assertThat(stateManager.deferredEvents.size).isEqualTo(3)

        sendEvent(stateMachineDefinition, e2, stateManager, HashMap())
        // inputting event e2 should cause deferred events e10 and e11
        // to automatically fire, leaving just 1 deferred event
        // remaining
        assertThat(stateManager.deferredEvents.size).isEqualTo(1)
        sendEvent(stateMachineDefinition, e9, stateManager, HashMap())
        assertThat(stateManager.deferredEvents.size).isEqualTo(0)
        sendEvent(stateMachineDefinition, e10, stateManager, HashMap())
        assertThat(stateManager.deferredEvents.size).isEqualTo(0)
        sendEvent(stateMachineDefinition, e4, stateManager, HashMap())
        sendEvent(stateMachineDefinition, e5, stateManager, HashMap())
        sendEvent(stateMachineDefinition, e3, stateManager, HashMap())
        assertThat(stateManager.deferredEvents.size).isEqualTo(0)

        assertThrows<UnprocessableEventException> { sendEvent(stateMachineDefinition, e3, stateManager, HashMap()) }

        sendEvent(stateMachineDefinition, e6, stateManager, HashMap()) //e6
        sendEvent(stateMachineDefinition, e1, stateManager, HashMap()) // e1
        sendEvent(stateMachineDefinition, e11, stateManager, HashMap())
    }

    /**
     * Sends an event to a stateful trade confirmation and prints the
     * elapsed processing time to System.out.
     */
    private fun sendEvent(stateMachineDefinition: IStateMachine,
                          event: IEvent,
                          stateManager: IStateManager,
                          context: Map<String, String>) {
        val startTime = System.currentTimeMillis()
        stateMachineDefinition.handleEvent(event, stateManager, context)
        val elapsedTime = System.currentTimeMillis() - startTime
        log.debug("Processed event " + event.type + " in " + elapsedTime + " ms.")
    }

}