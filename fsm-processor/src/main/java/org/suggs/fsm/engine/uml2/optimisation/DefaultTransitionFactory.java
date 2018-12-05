package org.suggs.fsm.engine.uml2.optimisation;

import org.suggs.fsm.engine.uml2.behaviorstatemachines.Transition;
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.behaviorstatemachines.IVertex;
import org.suggs.fsm.uml2.scribe.optimisation.ITransitionFactory;

/**
 * Default implementation of a transition factory.
 */
public class DefaultTransitionFactory implements ITransitionFactory {

    public ITransition createTransition(String transitionKind, IVertex incomingVertex, IVertex outgoingVertex) {

        ITransition transition = new Transition(transitionKind);

        transition.setIncomingVertex(incomingVertex);
        transition.setOutgoingVertex(outgoingVertex);

        return transition;
    }
}
