package org.suggs.fsm.behavior

import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.traits.Fireable
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

abstract class Transition(name: String,
                          val source: Vertex,
                          val target: Vertex,
                          val triggers: Set<Trigger>,
                          val guard: Constraint,
                          val effects: Set<Behavior>)
    : Fireable, Namespace(name) {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)!!
    }

    abstract fun doFire(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)

    override fun fire(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        Transition.log.debug("Internally transitioning from ${source.deriveQualifiedName()} to ${target.deriveQualifiedName()}")
        doFire(event, fsmExecutionContext)
    }

    fun isFireableFor(event: BusinessEvent): Boolean {
        return oneOfTheTriggersIsFiredBy(event.type) && guardEvaluatesFor(event)
    }

    private fun oneOfTheTriggersIsFiredBy(eventType: String): Boolean {
        return triggers.map { it.event }.contains(anEventCalled(eventType).build())
    }

    private fun guardEvaluatesFor(event: BusinessEvent): Boolean {
        return guard.evaluate(event)
    }
}

