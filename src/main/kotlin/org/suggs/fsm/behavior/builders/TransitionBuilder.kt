package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.*
import org.suggs.fsm.behavior.Event.Companion.COMPLETION_EVENT_NAME
import org.suggs.fsm.behavior.FinalState.Companion.DEFAULT_FINAL_STATE_NAME
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled
import org.suggs.fsm.execution.BusinessEvent

class TransitionBuilder(var name: String, val type: TransitionKind) {

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
    private var constraint: Constraint = EmptyGuardConstraint()

    init {
        triggers.add(aTriggerCalled(COMPLETION_EVENT_NAME).firedWith(anEventCalled(COMPLETION_EVENT_NAME)))
    }

    fun startingAt(stateName: String): TransitionBuilder {
        startState = stateName
        return this
    }

    fun startingAtInitialState(): TransitionBuilder {
        startState = PseudoStateKind.INITIAL.toString()
        return this
    }

    fun endingAt(stateName: String): TransitionBuilder {
        endState = stateName
        return this
    }

    fun endingAtFinalState(): TransitionBuilder {
        endState = DEFAULT_FINAL_STATE_NAME
        return this
    }

    fun triggeredBy(vararg newTriggers: TriggerBuilder): TransitionBuilder {
        triggers.removeIf { it.name == COMPLETION_EVENT_NAME }
        triggers.addAll(newTriggers)
        return this
    }

    fun triggeredBy(vararg events: EventBuilder): TransitionBuilder {
        triggers.removeIf { it.name == COMPLETION_EVENT_NAME }
        events.map { triggers.add(aTriggerCalled("").firedWith(it)) }
        return this
    }

    fun guardedBy(constraint: Constraint): TransitionBuilder{
        this.constraint = constraint
        return this
    }

    fun guardedBy(effect: (BusinessEvent) -> Boolean): TransitionBuilder{
        this.constraint = SimpleGuardConstraint("ANONYMOUS", effect)
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
                constraint,
                effects.map { it.build() }.toSet())
    }

}