package org.suggs.fsm.engine.uml2.optimisation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition
import org.suggs.fsm.uml2.behaviorstatemachines.IVertex

class DefaultTransitionFactoryTest {

    @Mock lateinit var incomingVertex: IVertex
    @Mock lateinit var outgoingVertex: IVertex

    @BeforeEach fun setUp() {
        initMocks(this)
    }

    @Test fun testCreateTransition() {
        val factory = DefaultTransitionFactory()

        val returnedTransition = factory.createTransition(ITransition.EXTERNAL, incomingVertex, outgoingVertex)

        assertThat(returnedTransition.transitionKind).isEqualTo(ITransition.EXTERNAL)
        assertThat(incomingVertex).isEqualTo(returnedTransition.incomingVertex)
        assertThat(outgoingVertex).isEqualTo(returnedTransition.outgoingVertex)

        assertThat(returnedTransition.outgoingVertex).isNotEqualTo(returnedTransition.incomingVertex)
    }
}