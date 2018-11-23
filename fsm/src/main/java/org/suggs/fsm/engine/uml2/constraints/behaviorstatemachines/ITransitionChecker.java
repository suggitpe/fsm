package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;

/**
 * Objects that implement this interface check transitions for
 * constraint violations.
 */
public interface ITransitionChecker {

    /**
     * Checks a transition.
     */
    void checkConstraints(ITransition transition);

}
