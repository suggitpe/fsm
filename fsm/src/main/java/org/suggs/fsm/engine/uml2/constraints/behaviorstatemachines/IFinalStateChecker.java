package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.uml2.behaviorstatemachines.IState;

/**
 * Objects that implement this interface check final states for
 * constraint violations.
 */
public interface IFinalStateChecker {

    /**
     * Checks the final state.
     */
    void checkConstraints(IState finalState);

}
