package org.suggs.fsm.behavior.builders

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory.getLogger
import org.suggs.fsm.behavior.Event.Companion.COMPLETION_EVENT_NAME
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.builders.BehaviorBuilder.Companion.aBehaviorCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.SimpleStateBuilder.Companion.aStateCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled
import org.suggs.fsm.stubs.RegionContainerStub.Companion.aRegionContainerStub

class TransitionBuilderTest {

    private val log = getLogger(this::class.java)

    private val region = aRegionCalled("testRegion").build(aRegionContainerStub())
    private val vertices: Map<String, Vertex> = listOf(aStateCalled("STATE1").build(region), aStateCalled("STATE2").build(region)).map { it.name to it }.toMap()

    private lateinit var transitionPrototype: TransitionBuilder

    @BeforeEach fun `set up prototype`() {
        transitionPrototype = anExternalTransitionCalled("transitionPrototype")
                .startingAt("STATE1")
                .endingAt("STATE2")
    }

    @Test fun `builds transitions with states`() {
        val transition = transitionPrototype.build(vertices)
        assertThat(transition.source.name).isEqualTo("STATE1")
        assertThat(transition.target.name).isEqualTo("STATE2")
    }

    @Test fun `builds transitions that have default COMPLETION event triggers`() {
        val transition = transitionPrototype.build(vertices)
        assertThat(transition.triggers.size).isEqualTo(1)
        assertThat(transition.triggers.first().name).isEqualTo(COMPLETION_EVENT_NAME)
    }

    @Test fun `builds transitions with triggers`() {
        val transition = transitionPrototype
                .triggeredBy(aTriggerCalled("TRIGGER1"), aTriggerCalled("TRIGGER2"))
                .build(vertices)
        assertThat(transition.triggers.size).isEqualTo(2)
        assertThat(transition.triggers.sortedBy { it.name }.first().name).isEqualTo("TRIGGER1")
        assertThat(transition.triggers.sortedBy { it.name }.last().name).isEqualTo("TRIGGER2")
    }

    @Test fun `builds transitions with effects`() {
        val transition = transitionPrototype
                .withEffects(aBehaviorCalled("BEHAVIOR1").withAction { log.debug(it.toString()) })
                .build(vertices)
        assertThat(transition.effects.size).isEqualTo(1)
        assertThat(transition.effects.first().name).isEqualTo("BEHAVIOR1")
    }


}