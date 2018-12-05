package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion
import org.suggs.fsm.uml2.behaviorstatemachines.IState
import org.suggs.fsm.uml2.behaviorstatemachines.IState.SIMPLE
import org.suggs.fsm.uml2.behaviorstatemachines.IState.SIMPLE_COMPOSITE

class StateCheckerTest {
    private lateinit var stateChecker: IStateChecker

    @Mock lateinit var state: IState
    @Mock lateinit var region: IRegion

    @BeforeEach fun setUp() {
        stateChecker = StateChecker()
        initMocks(this)
    }

    @Test fun testNoRegionInSimpleState() {
        `when`(state.region).thenReturn(region)
        `when`(state.stateKind).thenReturn(SIMPLE)

        assertThrows<IllegalStateException> { stateChecker.checkConstraints(state) }
    }

    @Test
    fun testRegionInCompositeState() {
        `when`(state.region).thenReturn(null)
        `when`(state.stateKind).thenReturn(SIMPLE_COMPOSITE)

        assertThrows<IllegalStateException> { stateChecker.checkConstraints(state) }
    }
}