package org.suggs.fsm.engine.uml2.basicbehaviors

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.basicbehaviors.IBehavior
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser

class BehavioredClassifierTest {

    private var behavioredClassifier = BehavioredClassifier()

    @Mock private lateinit var behaviour: IBehavior
    @Mock private lateinit var constraintChecker: IConstraintVisitor

    @BeforeEach fun `reset mocks`() {
        initMocks(this)
    }

    @Test fun `test accessors stupid test`() {
        behavioredClassifier.ownedBehavior = behaviour
        assertThat(behavioredClassifier.ownedBehavior).isEqualTo(behaviour)
    }

    @Test fun `test constraint checker acceptance`() {
        behavioredClassifier.acceptConstraintVisitor(constraintChecker)
        verify(constraintChecker).visitBehavioredClassifier(behavioredClassifier)
    }

    @Test fun `test optimiser acceptance`() {
        val modelOptimiser = mock(IModelOptimiser::class.java)

        // Test 1:
        // calling BehavioredClassifier.acceptOptimiser(IModelOptimiser) without first setting the
        // owned Behavior should cause null pointer exception to be thrown
        assertThrows<NullPointerException> { behavioredClassifier.acceptOptimiser(modelOptimiser) }

        // Test 2:
        // after setting the owned behaviour, calling BehavioredClassifier.acceptOptimiser(IModelOptimiser)
        // should invoke acceptOptimiser on the owned behaviour
        behavioredClassifier.ownedBehavior = behaviour
        behavioredClassifier.acceptOptimiser(modelOptimiser)

        verify(behaviour).setContext(behavioredClassifier)
        verify(behaviour).acceptOptimiser(modelOptimiser)
    }

    @Test fun `test namespace object manager acceptance`() {
        val namespaceObjMgr = mock(INamespaceObjectManager::class.java)

        // Test 1:
        // calling BehavioredClassifier.acceptNamespaceObjectManager(INamespaceObjectManager)
        // without first setting the owned Behavior will cause null pointer exception to be thrown
        assertThrows<NullPointerException> { behavioredClassifier.acceptNamespaceObjectManager(namespaceObjMgr) }

        // Test 2:
        // after setting the owned behaviour,calling
        // BehavioredClassifier.acceptNamespaceObjectManager(INamespaceObjectManager)
        // should invoke acceptNamespaceObjectManager on the owned behaviour
        behavioredClassifier.ownedBehavior = behaviour
        behavioredClassifier.acceptNamespaceObjectManager(namespaceObjMgr)

        verify(behaviour).setContext(behavioredClassifier)
        verify(behaviour).acceptNamespaceObjectManager(namespaceObjMgr)
    }
}