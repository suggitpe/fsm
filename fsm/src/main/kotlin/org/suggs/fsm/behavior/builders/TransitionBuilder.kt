package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Transition
import org.suggs.fsm.behavior.Trigger
import org.suggs.fsm.behavior.Vertex

class TransitionBuilder(val name: String) {

    companion object {
        fun anExternalTransitionCalled(name: String): TransitionBuilder {
            return TransitionBuilder(name)
        }
    }

    val triggers: MutableSet<TriggerBuilder> = HashSet()
    lateinit var startState: String
    lateinit var endState: String

    fun startingAt(stateName: String): TransitionBuilder {
        startState = stateName
        return this
    }

    fun endingAt(stateName: String): TransitionBuilder {
        endState = stateName
        return this
    }

    fun withTriggers(vararg newTriggers: TriggerBuilder): TransitionBuilder {
        triggers.addAll(newTriggers)
        return this
    }

    fun build(vertices: Map<String, Vertex>): Transition {
        val transition = Transition(name, vertices[startState]!!, vertices[endState]!!)
        transition.addTriggers(triggers.map { it.build() }.toSet())
        return transition
    }

}