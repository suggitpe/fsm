package org.suggs.fsm.engine.uml2.behaviorstatemachines

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion
import org.suggs.fsm.uml2.behaviorstatemachines.IState
import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition

class RegionTest {

    private val STATE1_NAME = "S1"
    private val STATE2_NAME = "S2"
    private val TRANSTION1_NAME = "T1"
    private val TRANSTION2_NAME = "T2"

    private lateinit var region: IRegion

    @Mock lateinit var enclosingState: IState
    @Mock lateinit var stateMachine: IStateMachine
    @Mock lateinit var transition1: ITransition
    @Mock lateinit var transition2: ITransition
    @Mock lateinit var state1: IState
    @Mock lateinit var state2: IState
    private lateinit var subVertices: Set<IState>
    private lateinit var transitions: Set<ITransition>

    @BeforeEach internal fun `set up`() {
        initMocks(this)
        region = Region()

        `when`(transition1.name).thenReturn(TRANSTION1_NAME)
        `when`(transition2.name).thenReturn(TRANSTION2_NAME)
        `when`(state1.name).thenReturn(STATE1_NAME)
        `when`(state2.name).thenReturn(STATE2_NAME)

        subVertices = HashSet(listOf(state1, state2))
        transitions = HashSet(listOf(transition1, transition2))
    }

    /**
     * Simple accessor tests
     */
    @Test internal fun `accessors stupid test`() {
        region.state = enclosingState

        region.stateMachine = stateMachine

        state1.namespace = region
        state1.container = region
        state2.namespace = region
        state2.container = region

        region.subVertices = subVertices

        transition1.namespace = region
        transition1.container = region
        transition2.namespace = region
        transition2.container = region

        region.transitions = transitions

        assertThat(region.state).isEqualTo(enclosingState)
        assertThat(region.stateMachine).isEqualTo(stateMachine)
        assertThat(region.subVertices).isEqualTo(subVertices)
        assertThat(region.transitions).isEqualTo(transitions)
    }

}