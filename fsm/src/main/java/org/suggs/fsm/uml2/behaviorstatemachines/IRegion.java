package org.suggs.fsm.uml2.behaviorstatemachines;

import org.suggs.fsm.uml2.kernel.INamespace;
import org.suggs.fsm.uml2.scribe.optimisation.IOptimisable;
import org.suggs.fsm.uml2.scribe.runtime.IEntryReporter;

import java.util.List;
import java.util.Set;

/**
 * Defines an implementation of the UML 2.0 Region class. Regions are
 * containers for states and transitions.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface IRegion extends INamespace, IOptimisable, IEntryReporter {

    /**
     * Sets the transitions owned by this region. The method adds the
     * transitions to the owned memebers of this region and also sets
     * the namespace and container of the vertices to be this region.
     */
    void setTransitions(Set<ITransition> transitions);

    /**
     * @return The transitions owned by this region.
     */
    Set<ITransition> getTransitions();

    /**
     * Sets the vertices owned by this region. The method adds the
     * vertices to the owned memebers of this region and also sets the
     * namespace and container of the vertices to be this region.
     */
    void setSubVertices(Set<IState> vertices);

    /**
     * @return The vertices owned by this region
     */
    Set<IState> getSubVertices();

    /**
     * Sets the state machine that owns this region.
     */
    void setStateMachine(IStateMachine stateMachine);

    /**
     * If the region is owned by a state machine rather than a state,
     * returns the owning IStateMachine instance.
     */
    IStateMachine getStateMachine();

    /**
     * Sets the state that owns this region.
     */
    void setState(IState state);

    /**
     * If the region is owned by a state rather than a state machine,
     * returns the owning IState instance.
     */
    IState getState();

    /**
     * Returns the initial pseudoState of this region.
     */
    IPseudoState getInitialState();

    /**
     * Gets the regions enclosing this region in an ordered list,
     * innermost first.
     */
    List<IRegion> getAncestorList();

    /**
     * Adds a vertex to the set owned by the region. If the same
     * vertex is already owned by the region, based on an equals()
     * comparison, then it will not be added again.
     */
    void addSubVertex(IVertex vertex);
}
