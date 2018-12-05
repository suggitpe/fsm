package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.suggs.fsm.event.IEventFactory
import org.suggs.fsm.framework.spi.IFsmEventInterceptor
import org.suggs.fsm.framework.spi.IStateManager

class StateMachineContextTest {

    private val stateMachineContext = StateMachineContext()

    @Test fun `test accessors stupid test`() {

        val currentActiveState = "test"

        val stateManager = mock(IStateManager::class.java)
        `when`(stateManager.activeState).thenReturn(currentActiveState)

        val fsmEventInterceptor = mock(IFsmEventInterceptor::class.java)
        val eventFactory = mock(IEventFactory::class.java)

        stateMachineContext.setFsmEventInterceptor(fsmEventInterceptor)
        stateMachineContext.stateManager = stateManager
        stateMachineContext.eventFactory = eventFactory

        assertThat(stateMachineContext.stateManager.activeState).isEqualTo(currentActiveState)
        assertThat(stateMachineContext.eventInterceptor).isEqualTo(fsmEventInterceptor)
        assertThat(stateMachineContext.eventFactory).isEqualTo(eventFactory)
    }
}