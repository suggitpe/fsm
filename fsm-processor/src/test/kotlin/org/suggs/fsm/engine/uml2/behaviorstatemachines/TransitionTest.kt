package org.suggs.fsm.engine.uml2.behaviorstatemachines

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.basicbehaviors.IBehavior
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition
import org.suggs.fsm.uml2.behaviorstatemachines.IVertex
import org.suggs.fsm.uml2.communications.IEvent.COMPLETION_EVENT_QUALIFIED_NAME
import org.suggs.fsm.uml2.communications.ITrigger
import org.suggs.fsm.uml2.kernel.IConstraint

class TransitionTest {

    private lateinit var transition: ITransition

    @Mock lateinit var mockContainer: IRegion
    @Mock lateinit var mockBehavior1: IBehavior
    @Mock lateinit var mockBehavior2: IBehavior
    @Mock lateinit var mockGuard: IConstraint
    @Mock lateinit var mockIncomingVertex: IVertex
    @Mock lateinit var mockOutgoingVertex: IVertex
    @Mock lateinit var mockTrigger1: ITrigger
    @Mock lateinit var mockTrigger2: ITrigger

    @BeforeEach fun `set up`() {
        initMocks(this)
        transition = Transition("INTERNAL")
    }

    @Test fun `accessors stupid test`() {
        transition.container = mockContainer
        val effects = listOf(mockBehavior1, mockBehavior2)
        transition.effects = effects
        transition.guard = mockGuard
        transition.incomingVertex = mockIncomingVertex
        transition.outgoingVertex = mockOutgoingVertex
        val triggers = listOf(mockTrigger1, mockTrigger2)
        transition.triggers = triggers

        assertThrows<RuntimeException> { transition.transitionKind = "unknown" }

        assertThat(transition.container).isEqualTo(mockContainer)
        assertThat(transition.effects).isEqualTo(effects)
        assertThat(transition.guard).isEqualTo(mockGuard)
        assertThat(transition.incomingVertex).isEqualTo(mockIncomingVertex)
        assertThat(transition.outgoingVertex).isEqualTo(mockOutgoingVertex)

        transition.transitionKind = ITransition.EXTERNAL
        assertThat(transition.transitionKind).isEqualTo(ITransition.EXTERNAL)
        transition.transitionKind = ITransition.INTERNAL
        assertThat(transition.transitionKind).isEqualTo(ITransition.INTERNAL)
    }

    /**
     * Test that the transition has a single default trigger of completion event
     */
    @Test fun `default transition`() {
        assertThat(transition.triggers.size).isEqualTo(1)
        assertThat(transition.triggers[0].event.qualifiedName).isEqualTo(COMPLETION_EVENT_QUALIFIED_NAME)
    }
}