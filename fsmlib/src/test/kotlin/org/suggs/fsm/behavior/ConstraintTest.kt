package org.suggs.fsm.behavior

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.suggs.fsm.stubs.BusinessEventStub.Companion.aBusinessEventCalled

class ConstraintTest {

    @Test
    fun `EmptyGuardConstraints always evaluate to true`() {
        EmptyGuardConstraint().evaluate(aBusinessEventCalled("stub event")) shouldBe true
    }

    @Test
    fun `guard constrains evaluate to their function`() {
        SimpleGuardConstraint("Stub Domain Guard") { false }.evaluate(aBusinessEventCalled("stub event")) shouldBe false
    }

}