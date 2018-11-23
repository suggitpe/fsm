package org.suggs.fsm.engine.uml2.constraints.basicbehaviors;

import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;

/**
 * Objects that implement this interface check behaviored classifiers
 * for constraint violations.
 */
public interface IBehaviouredClassifierChecker {

    void checkConstraints(IBehavioredClassifier behavioredClassifier);

}
