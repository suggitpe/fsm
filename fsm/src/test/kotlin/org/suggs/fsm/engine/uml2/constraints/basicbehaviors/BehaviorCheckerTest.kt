package org.suggs.fsm.engine.uml2.constraints.basicbehaviors

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.suggs.fsm.uml2.basicbehaviors.IBehavior

class BehaviorCheckerTest {
    lateinit var behaviorChecker: IBehaviourChecker

    @BeforeEach
    fun `set up`() {
        behaviorChecker = BehaviourChecker()
    }

    @Test
    fun `constraint checks`() {
        val behavior = mock(IBehavior::class.java)
        behaviorChecker.checkConstraints(behavior)
    }
}