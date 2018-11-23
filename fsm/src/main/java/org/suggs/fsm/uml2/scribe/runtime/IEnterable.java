package org.suggs.fsm.uml2.scribe.runtime;

/**
 * An object that implements this interface may perform actions when
 * it is entered.
 */
public interface IEnterable
{

    /**
     * Enters the object without executing any entry action.
     */
    void enter( IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext );

    /**
     * Triggers the entry action for the object.
     */
    void doEntryAction( IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext );
}
