package org.suggs.fsm

import org.slf4j.LoggerFactory
import org.suggs.fsm.event.IEvent
import org.suggs.fsm.framework.spi.IFsmEventInterceptor
import org.suggs.fsm.uml2.basicbehaviors.IBehavior
import org.suggs.fsm.uml2.behaviorstatemachines.IState
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition
import org.suggs.fsm.uml2.kernel.IConstraint
import org.suggs.fsm.uml2.scribe.runtime.IEventContext
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext

class FsmEventInterceptorStub : IFsmEventInterceptor {
    private val log = LoggerFactory.getLogger(FsmEventInterceptorStub::class.java)

    override fun onActionExecuted(action: IBehavior, eventContext: IEventContext, namespaceContext: INamespaceContext, stateMachineContext: IStateMachineContext) {
        log.debug("++++ Action Executed: " + action.name + " ++++")
    }

    override fun onEventReceived(event: IEvent, context: Map<String, String>) {
        log.debug("++++ Event Received: " + event.type + " ++++")
    }

    override fun onEventSkipped(eventContext: IEventContext, namespaceContext: INamespaceContext, stateMachineContext: IStateMachineContext) {
        log.debug("++++ Event Skipped: " + eventContext.event.type + " ++++")
    }

    override fun onGuardEvaluated(guard: IConstraint, result: Boolean, eventContext: IEventContext, namespaceContext: INamespaceContext,
                                  stateMachineContext: IStateMachineContext) {
        log.debug("++++ Guard Evaluated: " + guard.guardEvaluator.name + " ++++")
    }

    override fun onTransitionComplete(transition: ITransition, sourceState: IState, targetState: IState, eventContext: IEventContext,
                                      namespaceContext: INamespaceContext, stateMachineContext: IStateMachineContext) {
        log.debug("++++ Transition Completed: " + transition.name + " ++++")
    }

    override fun onDeferredEventDiscarded(eventContext: IEventContext) {
        log.debug("++++ Deferred Event Discarded: " + eventContext.event.type + " ++++")
    }

    override fun onEventDeferred(eventContext: IEventContext) {
        log.debug("++++ Event Deferred: " + eventContext.event.type + " ++++")
    }

}