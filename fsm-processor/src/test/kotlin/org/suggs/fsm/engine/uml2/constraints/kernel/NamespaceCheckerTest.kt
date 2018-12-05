package org.suggs.fsm.engine.uml2.constraints.kernel

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.suggs.fsm.uml2.kernel.INamedElement
import org.suggs.fsm.uml2.kernel.INamespace
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager
import java.util.*

class NamespaceCheckerTest {

    private lateinit var namespaceChecker: INamespaceChecker

    @BeforeEach
    fun setUp() {
        namespaceChecker = NamespaceChecker()
    }

    @Test fun testDiscernableOwnedMembers() {
        val DISTINCT_NAMED_ELEMENTS = 20

        val ownedMembersWithDuplicates = HashSet<INamedElement>()
        for (i in 0..DISTINCT_NAMED_ELEMENTS) {
            ownedMembersWithDuplicates.add(MinimalNamedElement("Element$i"))
        }
        // Add a duplicate
        ownedMembersWithDuplicates.add(MinimalNamedElement("Element$DISTINCT_NAMED_ELEMENTS"))

        val namespace = mock(INamespace::class.java)
        `when`(namespace.ownedMembers).thenReturn(ownedMembersWithDuplicates)

        assertThrows<IllegalStateException> { namespaceChecker.checkConstraints(namespace) }
    }

    /**
     * Simple implementation of the INamedElement interface that only
     * supports get/set operations on Name.
     */
    internal inner class MinimalNamedElement(private var name_: String) : INamedElement {

        override fun getName(): String {
            return name_
        }

        override fun getNamespace(): INamespace {
            throw UnsupportedOperationException()
        }

        override fun getQualifiedName(): String {
            throw UnsupportedOperationException()
        }

        override fun setName(name: String) {
            name_ = name
        }

        override fun setNamespace(namespace: INamespace) {
            throw UnsupportedOperationException()
        }

        override fun acceptConstraintVisitor(constraintVisitor: IConstraintVisitor) {
            throw UnsupportedOperationException()
        }

        override fun acceptNamespaceObjectManager(namespaceObjectManager: INamespaceObjectManager) {
            throw UnsupportedOperationException()
        }
    }
}