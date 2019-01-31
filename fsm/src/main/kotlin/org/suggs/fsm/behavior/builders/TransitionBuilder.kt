package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.EmptyConstraint
import org.suggs.fsm.behavior.Event.Companion.COMPLETION_EVENT_NAME
import org.suggs.fsm.behavior.Transition
import org.suggs.fsm.behavior.TransitionKind
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled

class TransitionBuilder(val name: String, val type: TransitionKind) {

    companion object {
        // for transitions that
        fun anExternalTransitionCalled(name: String): TransitionBuilder {
            return TransitionBuilder(name, TransitionKind.EXTERNAL)
        }

        fun anInternalTransitionCalled(name: String): TransitionBuilder {
            return TransitionBuilder(name, TransitionKind.INTERNAL)
        }
    }

    private val triggers: MutableSet<TriggerBuilder> = HashSet()
    private val effects: MutableSet<BehaviorBuilder> = HashSet()
    lateinit var startState: String
    lateinit var endState: String

    init {
        triggers.add(aTriggerCalled(COMPLETION_EVENT_NAME).firedWith(anEventCalled(COMPLETION_EVENT_NAME)))
    }

    fun startingAt(stateName: String): TransitionBuilder {
        startState = stateName
        return this
    }

    fun endingAt(stateName: String): TransitionBuilder {
        endState = stateName
        return this
    }

    fun withTriggers(vararg newTriggers: TriggerBuilder): TransitionBuilder {
        triggers.removeIf { it.name == COMPLETION_EVENT_NAME }
        triggers.addAll(newTriggers)
        return this
    }

    fun withEffects(vararg newEffects: BehaviorBuilder): TransitionBuilder {
        effects.addAll(newEffects)
        return this
    }

    fun build(vertices: Map<String, Vertex>): Transition {
        return Transition(name,
                type,
                vertices.getValue(startState),
                vertices.getValue(endState),
                triggers.map { it.build() }.toSet(),
                EmptyConstraint(),
                effects.map { it.build() }.toSet())
    }

}