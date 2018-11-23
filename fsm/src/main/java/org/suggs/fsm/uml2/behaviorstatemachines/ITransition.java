/*
 * ITransition.java created on 02-Nov-2005 17:06:54 by billinro for project FsmLib
 *
 * Copyright (c) 2005 UBS Investment Bank
 */
package org.suggs.fsm.uml2.behaviorstatemachines;

import org.suggs.fsm.uml2.kernel.IConstraint;
import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.scribe.optimisation.IOptimisable;
import org.suggs.fsm.uml2.scribe.runtime.IEnableable;
import org.suggs.fsm.uml2.scribe.runtime.IFirable;

import java.util.List;

/**
 * Defines an implementation of the UML 2.0 Transition class.
 * Transitions represent a directed relationship between a source and
 * target vertex. They describe necessary conditions (guard
 * constraints) and actions that should occur when moving from the
 * source vertex to the target vertex.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface ITransition extends INamedElement, IFirable, IEnableable, IOptimisable {

    /**
     * The TransitionKind for an Internal Transition
     */
    String INTERNAL = "INTERNAL";

    /**
     * The TransitionKind for an external Transition
     */
    String EXTERNAL = "EXTERNAL";

    /**
     * Gets the triggers that can fire this transition.
     */
    List getTriggers();

    /**
     * Sets the triggers that can fire this transition. The ordering
     * of the list is maintained, such that common triggers can be
     * positioned at the start of the list and will be matched to
     * events more quickly.
     */
    void setTriggers(List triggers);

    /**
     * Returns the target vertex for this transition.
     */
    IVertex getOutgoingVertex();

    /**
     * Sets the target vertex for this transition.
     */
    void setOutgoingVertex(IVertex outgoingVertex);

    /**
     * Returns the source vertex for this transition.
     */
    IVertex getIncomingVertex();

    /**
     * Sets the source vertex for this transition.
     */
    void setIncomingVertex(IVertex incomingVertex);

    /**
     * Gets the transition actions for this transition.
     */
    List getEffects();

    /**
     * Sets the transition actions for this transition.
     */
    void setEffects(List effects);

    /**
     * Gets the kind of this transition, such as internal, external or
     * local.
     */
    String getTransitionKind();

    /**
     * Sets the kind of this transition, such as internal, external or
     * local.
     */
    void setTransitionKind(String transitionKind);

    /**
     * Gets the guard condition for this transition.
     */
    IConstraint getGuard();

    /**
     * Sets the guard condition for this transition.
     */
    void setGuard(IConstraint guard);

    /**
     * Sets the region that contains this transition.
     */
    void setContainer(IRegion container);

    /**
     * Gets the region that contains this transition.
     */
    IRegion getContainer();

    /**
     * Returns a shallow copy of the transition.
     */
    ITransition createShallowCopy();

}
