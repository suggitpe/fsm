package org.suggs.fsm.uml2.scribe.runtime;

/**
 * An object that implements this interface may perform actions when
 * it is exited as part of a transition.
 */
public interface IExitable
{

    /**
     * Exit the object, causing it to behave as appropriate. This may
     * include notifying other components that this object has been
     * exited but is does not include firing any exit actions.
     */
    void exit( IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext );

    /**
     * Triggers the exit action for the object.
     */
    void doExitAction( IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext );

}
