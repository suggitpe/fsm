package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState.*
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition
import java.util.stream.Collectors.toSet
import java.util.stream.IntStream.range

class PseudoStateCheckerTest {

    private lateinit var pseudoStateChecker: IPseudoStateChecker

    @Mock lateinit var pseudoState: IPseudoState

    @BeforeEach fun setUp() {
        pseudoStateChecker = PseudoStateChecker()
        initMocks(this)
    }

    /*
     * An pseudoStateKind vertex can have at most one outgoing transition
     */
    @Test
    fun testMultipleTransitionFromInitialVertexConstraint() {
        `when`(pseudoState.pseudoStateKind).thenReturn(INITIAL)
        `when`(pseudoState.outgoing).thenReturn(createMultipleTransitionSet(2))

        assertThrows<IllegalStateException> { pseudoStateChecker.checkConstraints(pseudoState) }
    }

    /*
     * An history vertex can have at most one outgoing transition
     */
    @Test
    fun testMultipleTransitionsFromShallowHistoryVertexConstraint() {
        `when`(pseudoState.pseudoStateKind).thenReturn(SHALLOW_HISTORY)
        `when`(pseudoState.outgoing).thenReturn(createMultipleTransitionSet(2))

        assertThrows<IllegalStateException> { pseudoStateChecker.checkConstraints(pseudoState) }
    }

    /*
     * An history vertex can have at most one outgoing transition
     */
    @Test
    fun testMultipleTransitionsFromDeepHistoryVertexConstraint() {
        `when`(pseudoState.pseudoStateKind).thenReturn(DEEP_HISTORY)
        `when`(pseudoState.outgoing).thenReturn(createMultipleTransitionSet(2))

        assertThrows<IllegalStateException> { pseudoStateChecker.checkConstraints(pseudoState) }
    }

    private fun createMultipleTransitionSet(transitionCount: Int): Set<ITransition> {
        return range(0, transitionCount).mapToObj { mock(ITransition::class.java) }.collect(toSet())
    }
}