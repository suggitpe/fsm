package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.basicbehaviors.IBehavior
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion
import org.suggs.fsm.uml2.behaviorstatemachines.IState
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition
import java.util.*

class FinalStateCheckerTest {

    private lateinit var finalStateChecker: IFinalStateChecker

    @Mock lateinit var transition: ITransition
    @Mock lateinit var finalState: IState
    @Mock lateinit var behaviour: IBehavior
    @Mock lateinit var region: IRegion

    @BeforeEach fun setUp() {
        finalStateChecker = FinalStateChecker()
        initMocks(this)
    }

    @Test fun testOutgoingTransitionConstraint() {
        val outgoingTransitions = HashSet<ITransition>()
        outgoingTransitions.add(transition)
        `when`(finalState.outgoing).thenReturn(outgoingTransitions)

        assertThrows<IllegalStateException> { finalStateChecker.checkConstraints(finalState) }
    }

    /*
     * A Final State cannot have entry behaviour A Final State cannot
     * have exit behaviour A Final State has no doActivity behavior
     */
    @Test fun testBehaviourConstraints() {
        `when`(finalState.outgoing).thenReturn(HashSet<ITransition>())
        `when`(finalState.region).thenReturn(region)
        `when`(finalState.entryBehavior).thenReturn(behaviour)
        `when`(finalState.exitBehavior).thenReturn(behaviour)

        assertThrows<IllegalStateException> { finalStateChecker.checkConstraints(finalState) }
    }
}