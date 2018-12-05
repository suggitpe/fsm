package org.suggs.fsm.framework.spi.impl

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SpringBehavioredClassifierBuilderTest {

    private lateinit var springBehavioredClassifierBuilder: SpringBehavioredClassifierBuilder

    @BeforeEach fun setUp() {
        springBehavioredClassifierBuilder = SpringBehavioredClassifierBuilder()
    }

    /**
     * Tests that an FsmRuntimeException is thrown if the state model definition file cannot be found.
     */
    @Test fun testStateModelNotFoundHandling() {
        assertThrows<RuntimeException> { springBehavioredClassifierBuilder.createBehavioredClassifier("") }
    }

    @Test fun testInvalidModelDefinitionHandling() {
        assertThrows<RuntimeException> { springBehavioredClassifierBuilder.createBehavioredClassifier("badBeanDefinition") }
    }

    @Test fun testNullStateModelIdHandling() {
        assertThrows<IllegalArgumentException> { springBehavioredClassifierBuilder.createBehavioredClassifier(null) }
    }

}