package org.suggs.fsm.framework.spi;

import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;

/**
 * Builds a UML BehavioredClassifier that contains a state machine
 * object graph.
 */
public interface IBehavioredClassifierBuilder
{

    /**
     * Creates a BehavioredClassifier for a given unique state model
     * specification identifier.
     */
    IBehavioredClassifier createBehavioredClassifier(String stateModelId );

}
