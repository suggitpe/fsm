package org.suggs.fsm.engine.uml2.kernel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.kernel.INamedElement
import org.suggs.fsm.uml2.kernel.INamespace
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor

class NamedElementTest {

    private lateinit var namedElement: INamedElement

    @Mock lateinit var constraintChecker: IConstraintVisitor
    @Mock lateinit var mockNamespace: INamespace

    @BeforeEach fun setUp() {
        namedElement = Constraint()
        initMocks(this)
    }

    @Test fun testAccessors() {
        val NAME = "name"
        val NAMESPACE = "namespace"

        namedElement.name = NAME
        assertThat(namedElement.name).isEqualTo(NAME)

        `when`(mockNamespace.name).thenReturn(NAMESPACE)
        namedElement.namespace = mockNamespace
        assertThat(namedElement.namespace).isEqualTo(mockNamespace)

        `when`(mockNamespace.qualifiedName).thenReturn(INamespace.TOP_LEVEL_NAMESPACE + INamespace.NAMESPACE_SEPARATOR + NAMESPACE)

        assertThat(namedElement.qualifiedName)
                .isEqualTo(INamespace.TOP_LEVEL_NAMESPACE + INamespace.NAMESPACE_SEPARATOR + NAMESPACE + INamespace.NAMESPACE_SEPARATOR + NAME)
    }

    @Test fun testConstraintCheckerAcceptance() {
        namedElement.acceptConstraintVisitor(constraintChecker)
        verify(constraintChecker).visitNamedElement(namedElement)
    }

}