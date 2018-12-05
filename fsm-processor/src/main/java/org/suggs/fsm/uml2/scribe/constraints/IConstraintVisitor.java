package org.suggs.fsm.uml2.scribe.constraints;

import org.suggs.fsm.engine.uml2.constraints.basicbehaviors.IBehaviourChecker;
import org.suggs.fsm.engine.uml2.constraints.basicbehaviors.IBehaviouredClassifierChecker;
import org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines.*;
import org.suggs.fsm.engine.uml2.constraints.communications.IEventChecker;
import org.suggs.fsm.engine.uml2.constraints.communications.ITriggerChecker;
import org.suggs.fsm.engine.uml2.constraints.kernel.INamedElementChecker;
import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.behaviorstatemachines.*;
import org.suggs.fsm.uml2.communications.IEvent;
import org.suggs.fsm.uml2.communications.ITrigger;
import org.suggs.fsm.uml2.kernel.INamedElement;

/**
 * Objects that implement this interface check elements of the UML
 * state machine metamodel for consistency with defined constraints.
 */
public interface IConstraintVisitor {

    /**
     * Checks a behavior for model constraint violations.
     *
     * @param behavior The behavior to check.
     */
    void visitBehavior(IBehavior behavior);

    /**
     * Checks a behaviored classifier and its owned behavior for model
     * constraint violations.
     *
     * @param behavioredClassifier The behaviored classifier to check.
     */
    void visitBehavioredClassifier(IBehavioredClassifier behavioredClassifier);

    /**
     * Checks an event for model constraint violations.
     *
     * @param event The event to check.
     */
    void visitEvent(IEvent event);

    /**
     * Checks a final state for model constraint violations.
     *
     * @param finalState The final state to check.
     */
    void visitFinalState(IState finalState);

    /**
     * Checks a named element for model constraint violations.
     *
     * @param namedElement The named element to check.
     */
    void visitNamedElement(INamedElement namedElement);

    /**
     * Checks a pseudostate for model constraint violations.
     *
     * @param pseudoState The pseudostate to check.
     */
    void visitPseudoState(IPseudoState pseudoState);

    /**
     * Checks a region and its owned objects for model constratint
     * violations.
     *
     * @param region The region to check.
     */
    void visitRegion(IRegion region);

    /**
     * Checks a state, including its entry and exit actions, for model
     * constraint violations.
     */
    void visitState(IState state);

    /**
     * Checks a state machine and its contained region for model
     * constraint violations.
     */
    void visitStateMachine(IStateMachine stateMachine);

    /**
     * Checks a transition, including its guards and actions for model
     * constraint violations.
     */
    void visitTransition(ITransition transition);

    /**
     * Checks a trigger, including its associated event, for model
     * constraint violations.
     */
    void visitTrigger(ITrigger trigger);

    /**
     * @return Returns the stateMachineChecker.
     */
    IStateMachineChecker getStateMachineChecker();

    /**
     * @param stateMachineChecker The stateMachineChecker to set.
     */
    void setStateMachineChecker(IStateMachineChecker stateMachineChecker);

    /**
     * @return Returns the behaviorChecker.
     */
    IBehaviourChecker getBehaviorChecker();

    /**
     * @param behaviorChecker The behaviorChecker to set.
     */
    void setBehaviorChecker(IBehaviourChecker behaviorChecker);

    /**
     * @return Returns the finalStateChecker.
     */
    IFinalStateChecker getFinalStateChecker_();

    /**
     * @param finalStateChecker The finalStateChecker to set.
     */
    void setFinalStateChecker(IFinalStateChecker finalStateChecker);

    /**
     * @return Returns the pseudoStateChecker.
     */
    IPseudoStateChecker getPseudoStateChecker();

    /**
     * @param pseudoStateChecker The pseudoStateChecker to set.
     */
    void setPseudoStateChecker(IPseudoStateChecker pseudoStateChecker);

    /**
     * @return Returns the namedElementChecker.
     */
    INamedElementChecker getNamedElementChecker();

    /**
     * @param namedElementChecker The namedElementChecker to set.
     */
    void setNamedElementChecker(INamedElementChecker namedElementChecker);

    /**
     * @return Returns the behavioredClassifierChecker.
     */
    IBehaviouredClassifierChecker getBehavioredClassifierChecker();

    /**
     * @param behavioredClassifierChecker The behavioredClassifierChecker to set.
     */
    void setBehavioredClassifierChecker(IBehaviouredClassifierChecker behavioredClassifierChecker);

    /**
     * @return Returns the regionChecker.
     */
    IRegionChecker getRegionChecker();

    /**
     * @param regionChecker The regionChecker to set.
     */
    void setRegionChecker(IRegionChecker regionChecker);

    /**
     * @return Returns the transitionChecker.
     */
    ITransitionChecker getTransitionChecker();

    /**
     * @param transitionChecker The transitionChecker to set.
     */
    void setTransitionChecker(ITransitionChecker transitionChecker);

    /**
     * @return Returns the stateChecker.
     */
    IStateChecker getStateChecker();

    /**
     * @param stateChecker The stateChecker to set.
     */
    void setStateChecker(IStateChecker stateChecker);

    /**
     * @return Returns the triggerChecker.
     */
    ITriggerChecker getTriggerChecker();

    void setTriggerChecker(ITriggerChecker triggerChecker);

    IEventChecker getEventChecker();

    void setEventChecker(IEventChecker eventChecker);

    IVertexChecker getVertexChecker();

    void setVertexChecker(IVertexChecker vertexChecker);
}
