package org.suggs.fsm.uml2.scribe.runtime;

import org.suggs.fsm.uml2.behaviorstatemachines.IState;

/**
 * Objects that implement this interface may be notified when a state
 * is entered. The interface is provided to support the implementation
 * of history states, which keep track of the last state entered in
 * the region that contains the history state (shallow history) and
 * also any regions enclosed within copound states (deep history).
 */
public interface IStateEntryListener {

    /**
     * Called to indicate that a state has been entered as part of a
     * transition.
     */
    void stateEntered(IState state, IStateMachineContext stateMachineContext);

}
