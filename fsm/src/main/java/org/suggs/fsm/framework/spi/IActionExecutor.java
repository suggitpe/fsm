package org.suggs.fsm.framework.spi;

import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

/**
 * Defines the contract for implementations of action executors.
 */
public interface IActionExecutor
{

    /**
     * Executes the action with the context provided. <p/>Action
     * executors should be <B>stateless </B>, since instances will be
     * re-used for different business object instances (which may be
     * passed in the eventContext).
     * <p>
     * NOTE: The implementation of this method <B>must execute on the
     * caller's thread </B> and may not start new threads. This is to
     * enable the caller of the state machine to maintain transaction
     * control over the executed actions.
     */
    void executeAction( IEventContext eventContext );

    /**
     * Gets the name of the action.
     */
    String getName();

    /**
     * Sets the name of the action. Each action name must be unique
     * within the state model.
     */
    void setName( String name );

}
