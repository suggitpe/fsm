package org.suggs.fsm.uml2.scribe.optimisation;

import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.behaviorstatemachines.IVertex;

/**
 * Objects that implement this interface create new <code>Transition</code> objects.
 */
public interface ITransitionFactory {

    /**
     * Creates a new transition of the specified kind with the incoming and outgoing vertexBuilders.
     */
    ITransition createTransition(String transitionKind, IVertex incomingVertex, IVertex outgoingVertex);

}
