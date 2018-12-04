package org.suggs.fsm.engine.uml2.behaviorstatemachines

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion
import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor

class StateMachineTest {
    private lateinit var stateMachine: IStateMachine

    @Mock lateinit var constraintChecker: IConstraintVisitor
    @Mock lateinit var region: IRegion

    @BeforeEach fun setup() {
        initMocks(this)
        stateMachine = StateMachine()
    }

    @Test fun `accessors stupid test`() {
        `when`(region.name).thenReturn("testRegion")
        stateMachine.ownedRegion = region
        assertThat(stateMachine.ownedRegion).isEqualTo(region)
    }

    @Test fun `constraint checker acceptance`() {
        stateMachine.acceptConstraintVisitor(constraintChecker)
        verify(constraintChecker).visitStateMachine(stateMachine)
    }
}