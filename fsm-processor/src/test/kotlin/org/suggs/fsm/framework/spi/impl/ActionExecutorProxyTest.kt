package org.suggs.fsm.framework.spi.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.framework.spi.IActionExecutor
import org.suggs.fsm.uml2.scribe.runtime.IEventContext

class ActionExecutorProxyTest {

    private lateinit var actionExecutor: IActionExecutor
    @Mock lateinit var eventContext: IEventContext

    @BeforeEach fun setUp() {
        actionExecutor = ActionExecutorProxy()
        initMocks(this)
    }

    @Test fun testAccessors() {
        val TEST_NAME = "test"
        actionExecutor.name = TEST_NAME
        assertThat(actionExecutor.name).isEqualTo(TEST_NAME)
    }

    @Test fun testFsmExceptionOnExecute() {
        assertThrows<RuntimeException> { actionExecutor.executeAction(eventContext) }
    }
}