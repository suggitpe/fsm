package org.suggs.fsm.engine.uml2.constraints.kernel

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.suggs.fsm.uml2.kernel.IConstraint

class ConstraintCheckerTest {
    private lateinit var constraintChecker: IConstraintChecker

    @BeforeEach
    fun setUp() {
        constraintChecker = ConstraintChecker()
    }

    @Test
    fun testNullGuardEvalauatorNotPermitted() {
        val constraint = mock<IConstraint>(IConstraint::class.java)
        `when`(constraint.guardEvaluator).thenReturn(null)

        assertThrows<IllegalStateException> { constraintChecker.checkConstraint(constraint) }
    }
}