package org.suggs.fsm.framework.spi;

import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.kernel.IConstraint;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

import java.util.Map;

/**
 * Defines the contract for a component that can be registered with
 * the FSM to intercept events state machine events. The interface is
 * provided for monitoring and debugging purposes only.
 * <p>
 * NOTE: Calls to the methods in this interface will be made from the
 * FSM event handling thread (i.e. the state machine caller's thread),
 * which will block until the method returns. Care must be taken when
 * writing interceptors to ensure that they do not interfere with the
 * event processing, for example by introducing delays or throwing
 * unchecked exceptions
 */
public interface IFsmEventInterceptor {

    /**
     * Called when an event is received by the FSM and before it is
     * handled.
     */
    void onEventReceived(IEvent event, Map context);

    /**
     * Called after an action has successfully executed.
     */
    void onActionExecuted(IBehavior action, IEventContext eventContext, INamespaceContext namespaceContext,
                          IStateMachineContext stateMachineContext);

    /**
     * Called after a guard has been successfully evaluated.
     */
    void onGuardEvaluated(IConstraint guard, boolean result, IEventContext eventContext, INamespaceContext namespaceContext,
                          IStateMachineContext stateMachineContext);

    /**
     * Called after an event has been skipped. This will occur if the
     * state machine has no transitions from the current state that
     * could be fired by the incoming event. This includes cases where
     * the guard condition evaluated to false.
     */
    void onEventSkipped(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext);

    /**
     * Called after a transition has successfully completed.
     */
    void onTransitionComplete(ITransition transition, IState sourceState, IState targetState, IEventContext eventContext,
                              INamespaceContext namespaceContext, IStateMachineContext stateMachineContext);

    /**
     * Called when a deferred event waiting in the deferred events
     * pool is removed from the pool because it is not in the set of
     * deferrable events of the current state, AND it does not trigger
     * any transition from the current state.
     */
    void onDeferredEventDiscarded(IEventContext eventContext);

    /**
     * Called when an event is deferred.
     */
    void onEventDeferred(IEventContext eventContext);

}
