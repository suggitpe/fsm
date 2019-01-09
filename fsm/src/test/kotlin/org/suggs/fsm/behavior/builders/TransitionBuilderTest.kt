package org.suggs.fsm.behavior.builders

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory.getLogger
import org.suggs.fsm.behavior.Transition
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.builders.BehaviorBuilder.Companion.aBehaviorCalled
import org.suggs.fsm.behavior.builders.StateBuilder.Companion.aStateCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled

class TransitionBuilderTest {

    private val log = getLogger(this::class.java)

    private val vertices: Map<String, Vertex> = listOf(aStateCalled("STATE1").build(), aStateCalled("STATE2").build()).map { it.name to it }.toMap()

    private val transition: Transition = anExternalTransitionCalled("T1")
            .startingAt("STATE1")
            .endingAt("STATE2")
            .withTriggers(aTriggerCalled("TRIGGER1"), aTriggerCalled("TRIGGER2"))
            .withEffects(aBehaviorCalled("BEHAVIOR1").withAction { it -> log.debug(it.toString()) })
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

    @Test
    fun `builds transitions with effects`() {
        assertThat(transition.effects.size).isEqualTo(1)
        assertThat(transition.effects.first().name).isEqualTo("BEHAVIOR1")
    }


}