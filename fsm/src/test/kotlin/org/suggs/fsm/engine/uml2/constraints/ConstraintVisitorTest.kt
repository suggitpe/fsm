package org.suggs.fsm.engine.uml2.constraints

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.engine.uml2.constraints.basicbehaviors.IBehaviourChecker
import org.suggs.fsm.engine.uml2.constraints.basicbehaviors.IBehaviouredClassifierChecker
import org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines.*
import org.suggs.fsm.engine.uml2.constraints.communications.IEventChecker
import org.suggs.fsm.engine.uml2.constraints.communications.ITriggerChecker
import org.suggs.fsm.engine.uml2.constraints.kernel.INamedElementChecker
import org.suggs.fsm.uml2.basicbehaviors.IBehavior
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor

class ConstraintVisitorTest {

    private lateinit var constraintVisitor: IConstraintVisitor

    // The mocks
    @Mock lateinit var behaviorChecker: IBehaviourChecker
    @Mock lateinit var behavioredClassifierChecker: IBehaviouredClassifierChecker
    @Mock lateinit var eventChecker: IEventChecker
    @Mock lateinit var finalStateChecker: IFinalStateChecker
    @Mock lateinit var namedElementChecker: INamedElementChecker
    @Mock lateinit var pseudoStateChecker: IPseudoStateChecker
    @Mock lateinit var regionChecker: IRegionChecker
    @Mock lateinit var stateChecker: IStateChecker
    @Mock lateinit var stateMachineChecker: IStateMachineChecker
    @Mock lateinit var transitionChecker: ITransitionChecker
    @Mock lateinit var triggerChecker: ITriggerChecker
    @Mock lateinit var vertexChecker: IVertexChecker

    @BeforeEach
    fun `set up`() {
        constraintVisitor = ConstraintVisitor()
        initMocks(this)
    }

    @Test
    fun `test accessors stupid test`() {
        // Set all the checker implementations
        setAllCheckers()

        // Check all the objects were set ok
        assertThat(constraintVisitor.behaviorChecker).isEqualTo(behaviorChecker)
        assertThat(constraintVisitor.behavioredClassifierChecker).isEqualTo(behavioredClassifierChecker)
        assertThat(constraintVisitor.eventChecker).isEqualTo(eventChecker)
        assertThat(constraintVisitor.finalStateChecker_).isEqualTo(finalStateChecker)
        assertThat(constraintVisitor.namedElementChecker).isEqualTo(namedElementChecker)
        assertThat(constraintVisitor.pseudoStateChecker).isEqualTo(pseudoStateChecker)
        assertThat(constraintVisitor.regionChecker).isEqualTo(regionChecker)
        assertThat(constraintVisitor.stateChecker).isEqualTo(stateChecker)
        assertThat(constraintVisitor.stateMachineChecker).isEqualTo(stateMachineChecker)
        assertThat(constraintVisitor.transitionChecker).isEqualTo(transitionChecker)
        assertThat(constraintVisitor.triggerChecker).isEqualTo(triggerChecker)
        assertThat(constraintVisitor.vertexChecker).isEqualTo(vertexChecker)
    }

    /**
     * Test that the constraint visitor applies the correct constraint checkers to an IBehavior subject. There
     * could be one of these tests for every type of element visited.
     */
    @Test
    fun testVisitBehavior() {
        setAllCheckers()

        val behavior = mock(IBehavior::class.java)

        constraintVisitor.visitBehavior(behavior)

        verify(behaviorChecker).checkConstraints(behavior)
        verify(namedElementChecker).checkConstraints(behavior)
    }

    /**
     * Uses all the setter methods to set the checkers.
     */
    @Test
    fun setAllCheckers() {
        constraintVisitor.behaviorChecker = behaviorChecker
        constraintVisitor.behavioredClassifierChecker = behavioredClassifierChecker
        constraintVisitor.eventChecker = eventChecker
        constraintVisitor.setFinalStateChecker(finalStateChecker)
        constraintVisitor.namedElementChecker = namedElementChecker
        constraintVisitor.pseudoStateChecker = pseudoStateChecker
        constraintVisitor.regionChecker = regionChecker
        constraintVisitor.stateChecker = stateChecker
        constraintVisitor.stateMachineChecker = stateMachineChecker
        constraintVisitor.transitionChecker = transitionChecker
        constraintVisitor.triggerChecker = triggerChecker
        constraintVisitor.vertexChecker = vertexChecker
    }
}