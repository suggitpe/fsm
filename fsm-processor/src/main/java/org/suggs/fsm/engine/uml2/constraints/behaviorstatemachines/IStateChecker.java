package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.uml2.behaviorstatemachines.IState;

/**
 * Objects that implement this interface check states for constraint
 * violations.
 */
public interface IStateChecker {

    /**
     * Checks the state.
     */
    void checkConstraints(IState state);

}
