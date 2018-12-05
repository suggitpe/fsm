package org.suggs.fsm.uml2.behaviorstatemachines;

import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.scribe.constraints.IConstrainedObject;
import org.suggs.fsm.uml2.scribe.optimisation.IOptimisable;

/**
 * Defines an implementation of the UML 2.0 StateMachine class.
 * StateMachines are used to express the behaviour of an object or
 * system in response to events.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface IStateMachine extends IBehavior, IConstrainedObject, IOptimisable {

    /**
     * Sets the region owned by this state machine.
     */
    void setOwnedRegion(IRegion ownedRegion);

    /**
     * Gets the region owned by this state machine.
     */
    IRegion getOwnedRegion();

    /**
     * @return The initial pseudostate for the state machine.
     */
    IPseudoState getInitialStableState();

}
