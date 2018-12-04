package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.engine.IEventContextFactory
import org.suggs.fsm.framework.spi.IStateManager
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager

class ScribeStateMachineTest {

    private val scribeStateMachine = ScribeStateMachine()

    @Mock private lateinit var eventContextFactory: IEventContextFactory
    @Mock private lateinit var namespaceObjectManager: INamespaceObjectManager
    @Mock private lateinit var stateManager: IStateManager

    @BeforeEach fun `reset mocks`() {
        initMocks(this)
    }

    @Test fun testAccessors() {

        scribeStateMachine.eventContextFactory = eventContextFactory
        assertThat(scribeStateMachine.eventContextFactory).isEqualTo(eventContextFactory)

        scribeStateMachine.namespaceObjectManager = namespaceObjectManager
        assertThat(scribeStateMachine.namespaceObjectManager).isEqualTo(namespaceObjectManager)

        /*
         * Expect the state machine to check the manager has a current
         * state and if history states map is null, to initialise it to
         * an empty map.
         */
        `when`(stateManager.historyStates).thenReturn(null)
        stateManager.storeHistoryStates(null)

    }
}