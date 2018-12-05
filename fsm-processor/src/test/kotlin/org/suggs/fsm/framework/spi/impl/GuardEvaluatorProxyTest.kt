package org.suggs.fsm.framework.spi.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.framework.spi.IGuardEvaluator
import org.suggs.fsm.uml2.scribe.runtime.IEventContext

class GuardEvaluatorProxyTest {

    private lateinit var guardEvaluator: IGuardEvaluator
    @Mock lateinit var eventContext: IEventContext

    @BeforeEach fun setUp() {
        guardEvaluator = GuardEvaluatorProxy()
        initMocks(this)
    }

    @Test fun testAccessors() {
        val TEST_NAME = "test"
        guardEvaluator.name = TEST_NAME
        assertThat(guardEvaluator.name).isEqualTo(TEST_NAME)
    }

    @Test fun testFsmExceptionOnExecute() {
        assertThrows<RuntimeException> { guardEvaluator.evaluateGuard(eventContext) }
    }
}