package org.suggs.fsm.uml2.scribe.runtime;

/**
 * Objects that implement this interface may respond to events and the
 * associated context. The interface is provided to add event handling
 * functionality to transitionBuilders.
 */
public interface IFirable {

    /**
     * Causes the object to execute behavior based on supplied event
     * and context.
     */
    void fire(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachinecontext);

}