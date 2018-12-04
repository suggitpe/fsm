package org.suggs.fsm.engine.uml2.behaviorstatemachines

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor

class PseudoStateTest {

    private lateinit var pseudoState: IPseudoState

    @Mock lateinit var constraintChecker: IConstraintVisitor

    @BeforeEach fun `set up`() {
        initMocks(this)
        pseudoState = PseudoState("ENTRY_POINT")
    }

    @Test fun `constraint checker acceptance`() {
        pseudoState.acceptConstraintVisitor(constraintChecker)
        verify(constraintChecker).visitPseudoState(pseudoState)
    }

    /**
     * Tests the accessors methods
     */
    @Test fun `accessors stupid test`() {
        pseudoState.pseudoStateKind = IPseudoState.INITIAL
        assertThat(pseudoState.pseudoStateKind).isEqualTo(IPseudoState.INITIAL)
    }

    /**
     * Tests that the derived isInitialPseudostate() method returns `true` when the pseudostate kind is INITIAL.
     */
    @Test fun `initial pseudo state`() {
        pseudoState.pseudoStateKind = IPseudoState.ENTRY_POINT
        assertThat(pseudoState.isInitialPseudostate).isFalse()

        pseudoState.pseudoStateKind = IPseudoState.INITIAL
        assertThat(pseudoState.isInitialPseudostate).isTrue()
    }
}