package org.suggs.fsm.engine.uml2.kernel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doThrow
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.framework.spi.IFsmEventInterceptor
import org.suggs.fsm.framework.spi.IGuardEvaluator
import org.suggs.fsm.uml2.kernel.IConstraint
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager
import org.suggs.fsm.uml2.scribe.runtime.IEventContext
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext

open class ConstraintTest {

    lateinit var constraint: IConstraint

    @Mock lateinit var guardEvaluator: IGuardEvaluator
    @Mock lateinit var eventContext: IEventContext
    @Mock lateinit var namespaceContext: INamespaceContext
    @Mock lateinit var stateMachineContext: IStateMachineContext
    @Mock lateinit var eventInterceptor: IFsmEventInterceptor
    @Mock lateinit var namespaceObjectManager: INamespaceObjectManager

    @BeforeEach fun setUp() {
        constraint = Constraint()
        initMocks(this)
    }

    /**
     * Basic accessor tests */
    @Test fun testAccessors() {
        constraint.guardEvaluator = guardEvaluator
        assertThat(constraint.guardEvaluator).isEqualTo(guardEvaluator)
    }

    /**
     * Test that the guard is called correctly and any event
     * interceptor is notified of the result of the call. */
    @Test fun testGuardEvaluation() {
        val guardName = "testGuard"

        `when`(guardEvaluator.name).thenReturn(guardName)
        `when`(namespaceContext.namespaceObjectManager).thenReturn(namespaceObjectManager)
        `when`(namespaceObjectManager.getObject(guardName)).thenReturn(guardEvaluator)
        `when`(guardEvaluator.evaluateGuard(eventContext)).thenReturn(true)
        `when`(stateMachineContext.eventInterceptor).thenReturn(eventInterceptor)

        //eventInterceptor.onGuardEvaluated(constraint, true, eventContext, namespaceContext, stateMachineContext)

        constraint.guardEvaluator = guardEvaluator

        assertThat(constraint.evaluate(eventContext, namespaceContext, stateMachineContext)).isTrue()
    }

    @Test fun testExceptionWrapping() {
        val guardName = "testGuard"
        val exception = NullPointerException()

        `when`(guardEvaluator.name).thenReturn(guardName)
        doThrow(exception).`when`(guardEvaluator).evaluateGuard(eventContext)
        `when`(namespaceContext.namespaceObjectManager).thenReturn(namespaceObjectManager)
        `when`(namespaceObjectManager.getObject(guardName)).thenReturn(guardEvaluator)

        constraint.guardEvaluator = guardEvaluator

        assertThrows<RuntimeException> { constraint.evaluate(eventContext, namespaceContext, stateMachineContext) }
    }
}