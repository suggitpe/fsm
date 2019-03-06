package org.suggs.fsm.behavior

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.stubs.BusinessEventStub.Companion.aBusinessEventCalled

class ConstraintTest {

    @Test fun `EmptyGuardConstraints always evaluate to true`(){
        assertThat(EmptyGuardConstraint().evaluate(aBusinessEventCalled("stub event"))).isEqualTo(true)
    }

    @Test fun `guard constrains evaluate to their function`(){
        assertThat(SimpleGuardConstraint {false}.evaluate(aBusinessEventCalled("stub event"))).isEqualTo(false)
    }

}