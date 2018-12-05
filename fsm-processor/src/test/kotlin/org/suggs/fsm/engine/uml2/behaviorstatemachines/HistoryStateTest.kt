package org.suggs.fsm.engine.uml2.behaviorstatemachines

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor

class HistoryStateTest {

    private lateinit var pseudoState: IPseudoState
    @Mock lateinit var constraintChecker: IConstraintVisitor

    @BeforeEach fun `set up`() {
        initMocks(this)
        pseudoState = HistoryState("SHALLOW_HISTORY")
    }

    @Test fun `constraint checker acceptance`() {
        pseudoState.acceptConstraintVisitor(constraintChecker)
        verify(constraintChecker).visitPseudoState(pseudoState)
    }

    /**
     * Tests the accessors methods
     */
    @Test fun `accessors stupid tests`() {
        pseudoState.pseudoStateKind = IPseudoState.DEEP_HISTORY
        assertThat(pseudoState.pseudoStateKind).isEqualTo(IPseudoState.DEEP_HISTORY)
    }

    /**
     * Tests that the derived isInitialPseudostate() method returns `false` for deep history states.
     */
    @Test fun `deep history is not initial pseudo state`() {
        pseudoState.pseudoStateKind = IPseudoState.DEEP_HISTORY
        assertThat(pseudoState.isInitialPseudostate).isFalse()
    }

    /**
     * Tests that the derived isInitialPseudostate() method returns `false` for shallow history states.
     */
    @Test fun `shallow history is not initial pseudo state`() {
        pseudoState.pseudoStateKind = IPseudoState.SHALLOW_HISTORY
        assertThat(pseudoState.isInitialPseudostate).isFalse()
    }

    /**
     * Tests that the pseudoState kind must be one of the history kinds.
     */
    @Test fun `initialisation with non history state kind fails`() {
        assertThrows<RuntimeException> { pseudoState.pseudoStateKind = IPseudoState.INITIAL }
        assertThrows<RuntimeException> { pseudoState.pseudoStateKind = IPseudoState.ENTRY_POINT }
        assertThrows<RuntimeException> { pseudoState.pseudoStateKind = IPseudoState.EXIT_POINT }
    }
}