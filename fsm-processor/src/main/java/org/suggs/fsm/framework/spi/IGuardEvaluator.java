package org.suggs.fsm.framework.spi;

import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

/**
 * Defines the contract for implementations of guard evaluators.
 */
public interface IGuardEvaluator {

    /**
     * Executes the guard with the context provided. <p/>Guard
     * Evaluators should be <B>stateless </B>, since instances will be
     * re-used for different business object instances (which may be
     * passed in the eventContext).
     * <p>
     * NOTE: The implementation of this method <b>must execute on the
     * caller's thread </b> and may not start new threads. This is to
     * enable the caller of the state machine to maintain transaction
     * control over the executed actions.
     * <p>
     * Guards should not modify the state machine context and should
     * not have side-effects on the system. The arguments to this
     * method are marked as <code>final</code> as a reminder but
     * this will not provide full protection.
     */
    boolean evaluateGuard(final IEventContext eventContext);

    /**
     * Gets the name of the guard.
     */
    String getName();

    /**
     * Sets the name of the guard. Each guard name must be unique
     * within the state model.
     */
    void setName(String name);
}
