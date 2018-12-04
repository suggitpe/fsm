package org.suggs.fsm.engine.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager

class NamespaceContextTest {

    private val namespaceContext = NamespaceContext()

    @Test fun `test accessors stupid test`() {
        val mockNamespaceObjectManager = mock<INamespaceObjectManager>(INamespaceObjectManager::class.java)
        namespaceContext.setNamepaceObjectManager(mockNamespaceObjectManager)
        assertThat(namespaceContext.namespaceObjectManager).isEqualTo(mockNamespaceObjectManager)
    }
}