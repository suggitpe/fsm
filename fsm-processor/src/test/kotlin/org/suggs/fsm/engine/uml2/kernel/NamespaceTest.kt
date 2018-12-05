package org.suggs.fsm.engine.uml2.kernel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.kernel.INamedElement
import org.suggs.fsm.uml2.kernel.INamespace
import java.util.*

class NamespaceTest {

    private lateinit var namespace: INamespace

    private val MEMBER1_NAME = "member1"
    private val MEMBER2_NAME = "member2"
    private val MEMBER3_NAME = "member3"

    @Mock lateinit var member1: INamedElement
    @Mock lateinit var member2: INamedElement
    @Mock lateinit var member3: INamedElement

    @BeforeEach fun setUp() {
        namespace = Namespace()
        initMocks(this)

        `when`(member1.name).thenReturn(MEMBER1_NAME)
        `when`(member2.name).thenReturn(MEMBER2_NAME)
        `when`(member3.name).thenReturn(MEMBER3_NAME)

        member1.namespace = namespace
        member2.namespace = namespace
        member3.namespace = namespace
    }

    /**
     * Simple accessor tests */
    @Test fun testAccessors() {
        val ownedMembers = HashSet<INamedElement>()

        ownedMembers.add(member1)
        ownedMembers.add(member2)
        ownedMembers.add(member3)

        namespace.ownedMembers = ownedMembers

        assertThat(namespace.ownedMembers.size).isEqualTo(3)
        val iter = ownedMembers.iterator()
        while (iter.hasNext()) {
            val element = iter.next() as INamedElement
            assertThat(namespace.ownedMembers.contains(element)).isTrue()
        }
    }

    /**
     * Test that non-duplicate, non-null members are added correctly */
    @Test fun testAddingValidOwnedMembers() {
        assertThat(namespace.ownedMembers.size).isEqualTo(0)

        namespace.addOwnedMember(member1)
        assertThat(namespace.ownedMembers.size).isEqualTo(1)

        namespace.addOwnedMember(member2)
        assertThat(namespace.ownedMembers.size).isEqualTo(2)

        namespace.addOwnedMember(member3)
        assertThat(namespace.ownedMembers.size).isEqualTo(3)
    }

    /**
     * Test that null owned members are rejected. */
    @Test fun testAddingNullOwnedMember() {
        assertThrows<RuntimeException> { namespace.addOwnedMember(null) }
    }
}