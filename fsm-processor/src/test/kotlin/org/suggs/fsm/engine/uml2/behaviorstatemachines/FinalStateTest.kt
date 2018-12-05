package org.suggs.fsm.engine.uml2.behaviorstatemachines

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor

class FinalStateTest {

    private lateinit var finalState: FinalState

    @Mock lateinit var constraintChecker: IConstraintVisitor
    @Mock lateinit var region: IRegion

    @BeforeEach fun `set up`() {
        initMocks(this)
        finalState = FinalState()
    }

    /**
     * Accessors are not tested since IFinalState adds no extra member variables to IState.
     */
    @Test fun `constraint checker acceptance`() {
        finalState.acceptConstraintVisitor(constraintChecker)
        verify(constraintChecker).visitFinalState(finalState)
    }

    /**
     * A final state cannot have any regions
     */
    @Test fun `region constraint`() {
        assertThrows<RuntimeException> { finalState.region = region }
    }
}