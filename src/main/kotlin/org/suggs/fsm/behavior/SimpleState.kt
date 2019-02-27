package org.suggs.fsm.behavior

import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext
import org.suggs.fsm.execution.UnprocessableEventException

class SimpleState(name: String,
                  container: Region,
                  private val deferrableTriggers: Set<Trigger>,
                  entryBehavior: Behavior,
                  exitBehavior: Behavior)
    : State(name, container, deferrableTriggers, entryBehavior, exitBehavior) {


    override fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        log.debug("Exiting state [$name]")
        fsmExecutionContext.stateManager.storeActiveState(deriveQualifiedName().replaceAfterLast("::", TRANSITIONING))
    }

    override fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        log.debug("Entering state [$name]")
        fsmExecutionContext.stateManager.storeActiveState(deriveQualifiedName())

        if (!fireCompletionEvent(event, fsmExecutionContext)) {
            val triggeringDeferredEvents = findEventsThatFireTransitionsFrom(fsmExecutionContext.stateManager.getDeferredEvents())
            if (triggeringDeferredEvents.isNotEmpty()) {
                val triggeredEvent = triggeringDeferredEvents.first().name
                fsmExecutionContext.stateManager.removeDeferredEvent(triggeredEvent)
                log.debug("Firing deferred event $triggeredEvent")
                processEvent(BusinessEvent(triggeredEvent, event.identifier), fsmExecutionContext)
            }
        }
    }

    private fun fireCompletionEvent(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext): Boolean {
        return fireInternalEvent(Event.COMPLETION_EVENT_NAME, event, fsmExecutionContext)
    }

    private fun fireInternalEvent(eventType: String, event: BusinessEvent, fsmExecutionContext: FsmExecutionContext): Boolean {
        val newEvent = BusinessEvent(eventType, event.identifier)
        log.debug("Firing internal event $newEvent")
        return try {
            processEvent(newEvent, fsmExecutionContext)
            true
        } catch (noInternalTransitions: UnprocessableEventException) {
            false
        }
    }

}