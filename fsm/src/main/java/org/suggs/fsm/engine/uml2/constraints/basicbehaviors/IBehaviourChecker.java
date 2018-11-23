package org.suggs.fsm.engine.uml2.constraints.basicbehaviors;

import org.suggs.fsm.uml2.basicbehaviors.IBehavior;

/**
 * Objects that implement this interface check behaviors for
 * constraint violations.
 */
public interface IBehaviourChecker {

    void checkConstraints(IBehavior behavior);

}
