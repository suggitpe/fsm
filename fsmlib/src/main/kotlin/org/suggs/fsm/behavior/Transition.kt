package org.suggs.fsm.behavior

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.TransitionKind.EXTERNAL
import org.suggs.fsm.behavior.TransitionKind.INTERNAL
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.traits.Fireable
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

class Transition(name: String,
                 val type: TransitionKind,
                 val source: Vertex,
                 val target: Vertex,
                 val triggers: Set<Trigger> = HashSet(),
                 val guard: Constraint = EmptyGuardConstraint(),
                 val effects: Set<Behavior> = HashSet())
    : Fireable, NamedElementContainer(name) {

    private val behaviour = initialiseBehaviourFrom(type)

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    private fun initialiseBehaviourFrom(kind: TransitionKind): TransitionBehaviour {
        return when (kind) {
            INTERNAL -> InternalTransitionBehavior()
            EXTERNAL -> ExternalTransitionBehaviour()
            else -> throw IllegalStateException("")
        }
    }

    override fun fire(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        behaviour.fire(this, event, fsmExecutionContext)
    }

    fun isFireableFor(event: BusinessEvent): Boolean {
        return oneOfTheTriggersIsFiredBy(event.type) && guardEvaluatesTrueFor(event)
    }

    private fun oneOfTheTriggersIsFiredBy(eventType: String): Boolean {
        return triggers.map { it.event }.contains(anEventCalled(eventType).build())
    }

    private fun guardEvaluatesTrueFor(event: BusinessEvent): Boolean {
        return guard.evaluate(event)
    }

    interface TransitionBehaviour {
        fun fire(transition: Transition, event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)
    }

    inner class ExternalTransitionBehaviour : TransitionBehaviour {
        override fun fire(transition: Transition, event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
            // TODO: region exit for composite state in the right order
            // TODO: call up the stack to run the exit action on owning states if necessary

            log.info("Transitioning [${event.identifier}] from state [${source.deriveQualifiedName()}] to state [${target.deriveQualifiedName()}] with [${event.type}] event")

            source.exit(event, fsmExecutionContext)
            source.doExitAction(event, fsmExecutionContext)

            effects.map { it.execute(event) }

            target.doEntryAction(event, fsmExecutionContext)
            target.enter(event, fsmExecutionContext)
        }
    }

    inner class InternalTransitionBehavior : TransitionBehaviour {
        override fun fire(transition: Transition, event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
            Transition.log.debug("Internally transitioning from [${source.name}] to [${target.name}]")

            source.exit(event, fsmExecutionContext)

            effects.map { it.execute(event) }

            target.enter(event, fsmExecutionContext)
        }

    }
}

