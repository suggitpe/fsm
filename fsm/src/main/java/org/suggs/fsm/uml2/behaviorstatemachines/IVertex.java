package org.suggs.fsm.uml2.behaviorstatemachines;

import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.scribe.optimisation.IOptimisable;
import org.suggs.fsm.uml2.scribe.runtime.IEnterable;
import org.suggs.fsm.uml2.scribe.runtime.IExitable;

import java.util.List;
import java.util.Set;

/**
 * Defines an implementation of the UML 2.0 Vertex class. Vertices are
 * abstract nodes in state machine graphs and act as sources and
 * targets of transitions.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface IVertex extends INamedElement, IEnterable, IExitable, IOptimisable {

    /**
     * Sets the highest priority outgoing transitions for this node.
     */
    void setOutgoing(Set outgoingTransitions);

    /**
     * Gets the outgoing transitions for this node.
     */
    Set getOutgoing();

    /**
     * Sets the incoming transitions for this node.
     */
    void setIncoming(Set incomingTransitions);

    /**
     * Gets the incoming transitions for this node.
     */
    Set getIncoming();

    /**
     * Sets the containing region for this node.
     */
    void setContainer(IRegion container);

    /**
     * Gets the containing region for this node.
     */
    IRegion getContainer();

    /**
     * Gets the regions enclosing this vertex in an ordered list,
     * innermost first.
     */
    List getAncestorList();

    /**
     * Add an incoming transition to the current set.
     */
    void addIncomingTransition(ITransition transition);

    /**
     * Add an outgoing transition to the current set.
     */
    void addOutgoingTransition(ITransition transition);

    /**
     * Returns all possible outgoing transitions from this vertex,
     * including those from enclosing states. The transitions are
     * presented as a list of sets, where the list defines the
     * priority of the transition set with position 0 being the
     * highest priority, ie. the transitions that are defined on this
     * vertex.
     */
    List getAllPossibleOutgoingTransitions();
}
