package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.engine.IEventContextFactory
import org.suggs.fsm.event.IEventFactory
import org.suggs.fsm.framework.spi.IBehavioredClassifierBuilder
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser

class StateMachineFactoryTest {

    private val modelId = "modelId"
    private val stateMachineFactory = StateMachineFactory()

    @Mock private lateinit var behavioredClassifierBuilder: IBehavioredClassifierBuilder
    @Mock private lateinit var constraintChecker: IConstraintVisitor
    @Mock private lateinit var modelOptimiser: IModelOptimiser
    @Mock private lateinit var eventContextFactory: IEventContextFactory
    @Mock private lateinit var eventFactory: IEventFactory

    @BeforeEach fun `reset mocks`() {
        initMocks(this)
    }

    /**
     * Tests that creating a state machine performs the correct steps
     */
    @Test fun `creates StateMachines`() {
        val behavioredClassifier = mock(IBehavioredClassifier::class.java)
        `when`(behavioredClassifier.name).thenReturn("context")
        `when`(behavioredClassifierBuilder.createBehavioredClassifier(modelId)).thenReturn(behavioredClassifier)

        stateMachineFactory.constraintChecker = constraintChecker
        stateMachineFactory.behavioredClassifierBuilder = behavioredClassifierBuilder
        stateMachineFactory.eventContextFactory = eventContextFactory
        stateMachineFactory.eventFactory = eventFactory

        val stateMachine = stateMachineFactory.createStateMachine(modelId)

        assertThat(stateMachine).isNotNull
    }

    @Test fun `testAccessors stupid test`() {
        stateMachineFactory.behavioredClassifierBuilder = behavioredClassifierBuilder
        assertThat(stateMachineFactory.behavioredClassifierBuilder).isEqualTo(behavioredClassifierBuilder)

        stateMachineFactory.constraintChecker = constraintChecker
        assertThat(stateMachineFactory.constraintChecker).isEqualTo(constraintChecker)

        stateMachineFactory.modelOptimiser = modelOptimiser
        assertThat(stateMachineFactory.modelOptimiser).isEqualTo(modelOptimiser)

        stateMachineFactory.eventContextFactory = eventContextFactory
        assertThat(stateMachineFactory.eventContextFactory).isEqualTo(eventContextFactory)

        stateMachineFactory.eventFactory = eventFactory
        assertThat(stateMachineFactory.eventFactory).isEqualTo(eventFactory)
    }

}