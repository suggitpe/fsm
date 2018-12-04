package org.suggs.fsm.engine.uml2.basicbehaviors

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.framework.spi.IActionExecutor
import org.suggs.fsm.framework.spi.IFsmEventInterceptor
import org.suggs.fsm.uml2.basicbehaviors.IBehavior
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser
import org.suggs.fsm.uml2.scribe.runtime.IEventContext
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext

class BehaviorTest {
    private var behavior: IBehavior = Behavior()

    @Mock private lateinit var context: IBehavioredClassifier
    @Mock private lateinit var actionExecutor: IActionExecutor
    @Mock private lateinit var eventContext: IEventContext
    @Mock private lateinit var namespaceContext: INamespaceContext
    @Mock private lateinit var stateMachineContext: IStateMachineContext
    @Mock private lateinit var eventInterceptor: IFsmEventInterceptor
    @Mock private lateinit var namespaceObjectManager: INamespaceObjectManager
    @Mock private lateinit var constraintChecker: IConstraintVisitor

    @BeforeEach fun `reset mocks`() {
        initMocks(this)
    }

    @Test fun `test Accessors stupid test`() {
        behavior.context = context
        assertThat(behavior.context).isEqualTo(context)
    }

    /**
     * Tests that the constraint checker is accepted and called correctly
     */
    @Test fun `constraint checker acceptance`() {
        behavior.acceptConstraintVisitor(constraintChecker)
        verify(constraintChecker).visitBehavior(behavior)
    }

    @Test fun `optimiser acceptance`() {
        val modelOptimiser = mock(IModelOptimiser::class.java)

        behavior.acceptOptimiser(modelOptimiser)
        verify(modelOptimiser).optimiseBehavior(behavior)
    }

    @Test fun `execute test`() {
        val actionName = "ActionName"

        `when`(actionExecutor.name).thenReturn(actionName)
        `when`(stateMachineContext.eventInterceptor).thenReturn(eventInterceptor)
        `when`(namespaceContext.namespaceObjectManager).thenReturn(namespaceObjectManager)
        `when`(namespaceObjectManager.getObject(actionName)).thenReturn(actionExecutor)

        behavior.actionExecutor = actionExecutor

        // Call execute
        behavior.execute(eventContext, namespaceContext, stateMachineContext)
    }

    @Test fun testExceptionWrapping() {
        val actionName = "ActionName"
        val exception = NullPointerException()

        // Set the action executor to throw an NPE
        doThrow(exception).`when`(actionExecutor).executeAction(eventContext)

        `when`(actionExecutor.name).thenReturn(actionName)
        `when`(stateMachineContext.eventInterceptor).thenReturn(eventInterceptor)
        `when`(namespaceContext.namespaceObjectManager).thenReturn(namespaceObjectManager)
        `when`(namespaceObjectManager.getObject(actionName)).thenReturn(actionExecutor)

        behavior.actionExecutor = actionExecutor

        // Call execute
        assertThrows<RuntimeException> { behavior.execute(eventContext, namespaceContext, stateMachineContext) }
    }
}