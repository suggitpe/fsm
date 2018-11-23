package org.suggs.fsm.uml2.behaviorstatemachines;

import org.suggs.fsm.uml2.scribe.constraints.IConstrainedObject;

/**
 * Defines an implementation of the UML 2.0 PseudoState class.
 * PseudoStates are transient vertives in the state machine graph,
 * i.e. they are not stable states.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface IPseudoState extends IVertex, IConstrainedObject {

    /**
     * The PseudoStateKind for an Initial State
     */
    String INITIAL = "INITIAL";

    /**
     * The PseudoStateKind for a Deep History State
     */
    String DEEP_HISTORY = "DEEP_HISTORY";

    /**
     * The PseudoStateKind for a Shallow History State
     */
    String SHALLOW_HISTORY = "SHALLOW_HISTORY";

    /**
     * The PseudoStateKind for an Entry Point State
     */
    String ENTRY_POINT = "ENTRY_POINT";

    /**
     * The PseudoStateKind for an Exit Point State
     */
    String EXIT_POINT = "EXIT_POINT";

    /**
     * Gets the type of pseudostate represented by the object.
     *
     * @return The PseudoStateKind for this pseudostate
     */
    String getPseudoStateKind();

    /**
     * Sets the type of pseudoState.
     */
    void setPseudoStateKind(String pseudoStateKind);

    /**
     * @return <code>true</code> if this is a pseudoState of kind
     * IPseudoState.INITIAL, <code>false</code> otherwise
     */
    boolean isInitialPseudostate();
}
