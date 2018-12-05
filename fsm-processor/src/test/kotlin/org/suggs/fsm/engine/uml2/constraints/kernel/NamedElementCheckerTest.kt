package org.suggs.fsm.engine.uml2.constraints.kernel

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.kernel.INamedElement
import org.suggs.fsm.uml2.kernel.INamespace
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager

class NamedElementCheckerTest {

    private lateinit var namedElementChecker: INamedElementChecker
    @Mock lateinit var mockNamedElement: INamedElement

    @BeforeEach
    internal fun setUp() {
        namedElementChecker = NamedElementChecker()
        initMocks(this)
    }

    /** If there is no name there is no qualified name. */
    @Test fun testNoQualifiedNameIfNoName() {
        `when`(mockNamedElement.name).thenReturn("")
        `when`(mockNamedElement.qualifiedName).thenReturn("non-empty-string")

        assertThrows<IllegalStateException> { namedElementChecker.checkConstraints(mockNamedElement) }
    }

    /** If one of the containing namespaces has no name, there is no qualified name. */
    @Test
    internal fun testNoQualifiedNameIfNoNameInTree() {

        val testNamedElement = object : INamedElement {

            private var namespace: INamespace? = null

            override fun getName(): String {
                return "name"
            }

            override fun setName(name: String) {}

            override fun getQualifiedName(): String {
                return "non-empty"
            }

            override fun getNamespace(): INamespace? {
                return namespace
            }

            override fun setNamespace(namespace: INamespace) {
                this.namespace = namespace
            }

            override fun acceptConstraintVisitor(constraintChecker: IConstraintVisitor) {
                constraintChecker.visitNamedElement(this)
            }

            override fun acceptNamespaceObjectManager(namespaceObjectManager: INamespaceObjectManager) {
                // Do nothing
            }
        }

        val mockNamespace = mock(INamespace::class.java)
        `when`(mockNamespace.namespace).thenReturn(null)
        `when`(mockNamespace.name).thenReturn(INamespace.EMPTY_NAME)
        `when`(mockNamespace.qualifiedName).thenReturn(INamespace.EMPTY_NAME)

        testNamedElement.name = "name"
        testNamedElement.setNamespace(mockNamespace)

        assertThrows<IllegalStateException> { namedElementChecker.checkConstraints(testNamedElement) }
    }

    /** When there is a name, and all of the containing namespaces have a name, the qualified name is
     * constructed from the names of the containing namespaces. */
    @Test
    fun testQualifiedNameConstruction() {

        val mockNamespace = mock(INamespace::class.java)
        `when`(mockNamespace.namespace).thenReturn(null)
        `when`(mockNamespace.name).thenReturn("namespace")
        `when`(mockNamespace.qualifiedName).thenReturn("top::namespace")

        /* Mock a named element where the qualified name does not match the concatenation of name
         * and namespace qualified name */
        val mockNamedElement = mock(INamedElement::class.java)
        `when`(mockNamedElement.name).thenReturn("name")
        `when`(mockNamedElement.qualifiedName).thenReturn("wrong-qualified-name")
        `when`(mockNamedElement.namespace).thenReturn(mockNamespace)

        assertThrows<IllegalStateException> { namedElementChecker.checkConstraints(mockNamedElement) }

    }
}