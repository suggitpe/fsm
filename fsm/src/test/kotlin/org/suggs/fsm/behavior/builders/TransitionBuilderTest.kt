package org.suggs.fsm.behavior.builders

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.Transition
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.builders.StateBuilder.Companion.aStateCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled

class TransitionBuilderTest {


    private val vertices: Map<String, Vertex> = listOf(aStateCalled("STATE1").build(), aStateCalled("STATE2").build()).map { it.name to it }.toMap()

    private val transition: Transition = anExternalTransitionCalled("T1")
            .startingAt("STATE1")
            .endingAt("STATE2")
            .withTriggers(aTriggerCalled("TRIGGER1"), aTriggerCalled("TRIGGER2"))
            .build(vertices)

    @Test
    fun `builds transitions with states`() {
        assertThat(transition.source.name).isEqualTo("STATE1")
        assertThat(transition.target.name).isEqualTo("STATE2")
    }

    @Test
    fun `builds transitions with triggers`() {
        assertThat(transition.triggers.size).isEqualTo(2)
        assertThat(transition.triggers.sortedBy { it.name }.first().name).isEqualTo("TRIGGER1")
        assertThat(transition.triggers.sortedBy { it.name }.last().name).isEqualTo("TRIGGER2")
    }


}