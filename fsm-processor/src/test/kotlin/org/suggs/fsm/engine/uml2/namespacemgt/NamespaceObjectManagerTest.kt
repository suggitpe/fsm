package org.suggs.fsm.engine.uml2.namespacemgt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.basicbehaviors.IBehavior
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier
import org.suggs.fsm.uml2.behaviorstatemachines.*
import org.suggs.fsm.uml2.communications.ITrigger
import org.suggs.fsm.uml2.kernel.IConstraint
import org.suggs.fsm.uml2.kernel.INamedElement
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager
import java.util.*
import kotlin.collections.HashSet

class NamespaceObjectManagerTest {

    private lateinit var namespaceObjectManager: INamespaceObjectManager

    @Mock lateinit var region: IRegion
    @Mock lateinit var behavioredClassifier: IBehavioredClassifier
    @Mock lateinit var namedElement: INamedElement
    @Mock lateinit var transition: ITransition
    @Mock lateinit var vertex: IVertex
    @Mock lateinit var state: IState
    @Mock lateinit var entryBehavior: IBehavior
    @Mock lateinit var exitBevavior: IBehavior
    @Mock lateinit var stateMachine: IStateMachine
    @Mock lateinit var guard: IConstraint
    @Mock lateinit var effect: IBehavior
    @Mock lateinit var trigger: ITrigger

    @BeforeEach fun setUp() {
        namespaceObjectManager = NamespaceObjectManager()
        initMocks(this)
    }

    @Test fun testNullQualifiedNamesRejected() {
        assertThrows<RuntimeException> { namespaceObjectManager.addObject(null, Any()) }
    }

    @Test fun testAddObjects() {
        val numberOfObjectsToAdd = 100
        val controlObjectMap = HashMap<String, Any>()

        // Add objects with semi-random names
        for (i in 0 until numberOfObjectsToAdd) {
            val objectFullyQualifiedName = "" + Math.random()
            val thing = Object()

            controlObjectMap[objectFullyQualifiedName] = thing
            namespaceObjectManager.addObject(objectFullyQualifiedName, thing)
        }

        // Check the objects
        val iter = controlObjectMap.entries.iterator()
        while (iter.hasNext()) {
            val elem = iter.next()
            assertThat(namespaceObjectManager.getObject(elem.key)).isEqualTo(elem.value)
        }
    }

    /**
     * Tests that visiting a behaviored classifier calls the appropriate callback method. */
    @Test fun testVisitBehavioredClassifier() {
        namespaceObjectManager.visitBehavioredClassifier(behavioredClassifier)

        verify(behavioredClassifier).acceptNamespaceObjectManager(namespaceObjectManager)
    }

    /**
     * Tests that a GeneralRuntimeException is thrown if a call to `getObject` is made for an object that is not known. */
    @Test fun testGeneralRuntimeExceptionOnUnknownObject() {
        assertThrows<RuntimeException> { namespaceObjectManager.getObject("") }
    }

    /**
     * Tests that visiting a named element calls the appropriate callback method. */
    @Test fun testVisitNamedElement() {
        `when`(namedElement.qualifiedName).thenReturn("")

        namespaceObjectManager.registerElement(namedElement)
    }

    /**
     * Tests that visiting a region calls the appropriate callback method. */
    @Test fun testVisitRegion() {
        `when`(region.transitions).thenReturn(setOf(transition))
        `when`(region.subVertices).thenReturn(setOf(vertex))

        namespaceObjectManager.visitRegion(region)

        verify(transition).acceptNamespaceObjectManager(namespaceObjectManager)
        verify(vertex).acceptNamespaceObjectManager(namespaceObjectManager)
    }

    /**
     * Tests that visiting a atate calls the appropriate callback method. */
    @Test fun testVisitState() {

        region.acceptNamespaceObjectManager(namespaceObjectManager)

        `when`(state.region).thenReturn(region)
        `when`(state.entryBehavior).thenReturn(entryBehavior)
        `when`(state.exitBehavior).thenReturn(exitBevavior)
        `when`(state.deferrableTriggers).thenReturn(HashSet<ITrigger>())

        namespaceObjectManager.visitState(state)

        verify(entryBehavior).acceptNamespaceObjectManager(namespaceObjectManager)
        verify(exitBevavior).acceptNamespaceObjectManager(namespaceObjectManager)
    }

    /**
     * Tests that visiting a state machine calls the appropriate callback method. */
    @Test fun testVisitStateMachine() {
        `when`(stateMachine.ownedRegion).thenReturn(region)

        namespaceObjectManager.visitStateMachine(stateMachine)

        verify(region).acceptNamespaceObjectManager(namespaceObjectManager)
    }

    /**
     * Tests that visiting a transition calls the appropriate callback method and also visits the
     * constraints, guards and actions */
    @Test
    fun testVisitTransition() {
        `when`(transition.guard).thenReturn(guard)
        `when`(transition.effects).thenReturn(listOf(effect))
        `when`(transition.triggers).thenReturn(listOf(trigger))

        namespaceObjectManager.visitTransition(transition)

        verify(guard).acceptNamespaceObjectManager(namespaceObjectManager)
        verify(effect).acceptNamespaceObjectManager(namespaceObjectManager)
        verify(trigger).acceptNamespaceObjectManager(namespaceObjectManager)
    }
}