package org.suggs.fsm.engine.uml2.constraints;

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
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;

import java.util.List;
import java.util.Set;

public class ConstraintVisitor implements IConstraintVisitor {

    private IPseudoStateChecker pseudoStateChecker_;
    private IBehaviourChecker behaviorChecker_;
    private IBehaviouredClassifierChecker behavioredClassifierChecker_;
    private IEventChecker eventChecker_;
    private IFinalStateChecker finalStateChecker_;
    private IStateMachineChecker stateMachineChecker_;
    private INamedElementChecker namedElementChecker_;
    private IRegionChecker regionChecker_;
    private ITransitionChecker transitionChecker_;
    private IStateChecker stateChecker_;
    private ITriggerChecker triggerChecker_;
    private IVertexChecker vertexChecker_;

    public IStateMachineChecker getStateMachineChecker() {
        return stateMachineChecker_;
    }

    public void setStateMachineChecker(IStateMachineChecker stateMachineChecker) {
        this.stateMachineChecker_ = stateMachineChecker;
    }

    public IBehaviourChecker getBehaviorChecker() {
        return behaviorChecker_;
    }

    public void setBehaviorChecker(IBehaviourChecker behaviorChecker) {
        this.behaviorChecker_ = behaviorChecker;
    }

    public IFinalStateChecker getFinalStateChecker_() {
        return finalStateChecker_;
    }

    public void setFinalStateChecker(IFinalStateChecker finalStateChecker) {
        this.finalStateChecker_ = finalStateChecker;
    }

    public void visitRegion(IRegion region) {

        regionChecker_.checkConstraints(region);

        // Check sub-vertices
        Set<IVertex> vertices = region.getSubVertices();
        for (IVertex vertex : vertices) {
            vertex.acceptConstraintVisitor(this);
        }

        // Check transitions
        Set<ITransition> transitions = region.getTransitions();
        for (ITransition transition : transitions) {
            transition.acceptConstraintVisitor(this);
        }
    }

    public IPseudoStateChecker getPseudoStateChecker() {
        return pseudoStateChecker_;
    }

    public void setPseudoStateChecker(IPseudoStateChecker pseudoStateChecker) {
        this.pseudoStateChecker_ = pseudoStateChecker;
    }

    public void visitPseudoState(IPseudoState pseudoState) {
        pseudoStateChecker_.checkConstraints(pseudoState);
        namedElementChecker_.checkConstraints(pseudoState);
        vertexChecker_.checkConstraints(pseudoState);
    }

    public void visitBehavior(IBehavior behavior) {
        behaviorChecker_.checkConstraints(behavior);
        namedElementChecker_.checkConstraints(behavior);
    }

    public void visitBehavioredClassifier(IBehavioredClassifier behavioredClassifier) {
        behavioredClassifierChecker_.checkConstraints(behavioredClassifier);
        namedElementChecker_.checkConstraints(behavioredClassifier);

        // Visit the owned behavior
        behavioredClassifier.getOwnedBehavior().acceptConstraintVisitor(this);
    }

    public void visitFinalState(IState finalState) {
        finalStateChecker_.checkConstraints(finalState);
        namedElementChecker_.checkConstraints(finalState);
    }

    public void visitStateMachine(IStateMachine stateMachine) {
        stateMachineChecker_.checkConstraints(stateMachine);

        // visit owned region
        stateMachine.getOwnedRegion().acceptConstraintVisitor(this);
    }

    public void visitNamedElement(INamedElement namedElement) {
        namedElementChecker_.checkConstraints(namedElement);
    }

    public INamedElementChecker getNamedElementChecker() {
        return namedElementChecker_;
    }

    public void setNamedElementChecker(INamedElementChecker namedElementChecker) {
        this.namedElementChecker_ = namedElementChecker;
    }

    public IBehaviouredClassifierChecker getBehavioredClassifierChecker() {
        return behavioredClassifierChecker_;
    }

    public void setBehavioredClassifierChecker(IBehaviouredClassifierChecker behavioredClassifierChecker) {
        this.behavioredClassifierChecker_ = behavioredClassifierChecker;
    }

    public IRegionChecker getRegionChecker() {
        return regionChecker_;
    }

    public void setRegionChecker(IRegionChecker regionChecker) {
        this.regionChecker_ = regionChecker;
    }

    public ITransitionChecker getTransitionChecker() {
        return transitionChecker_;
    }

    public void setTransitionChecker(ITransitionChecker transitionChecker) {
        this.transitionChecker_ = transitionChecker;

    }

    public void visitTransition(ITransition transition) {
        transitionChecker_.checkConstraints(transition);
        namedElementChecker_.checkConstraints(transition);

        // Check owned effects
        List<IBehavior> effects = transition.getEffects();
        for (IBehavior behavior : effects) {
            behavior.acceptConstraintVisitor(this);
        }

        // Check owned guard
        if (null != transition.getGuard()) {
            transition.getGuard().acceptConstraintVisitor(this);
        }

        // check triggers
        List<ITrigger> triggers = transition.getTriggers();
        if (null != triggers) {
            for (ITrigger trigger : triggers) {
                trigger.acceptConstraintVisitor(this);
            }
        }

    }

    public IStateChecker getStateChecker() {
        return stateChecker_;
    }

    public void setStateChecker(IStateChecker stateChecker) {
        this.stateChecker_ = stateChecker;
    }

    public void visitState(IState state) {
        stateChecker_.checkConstraints(state);
        namedElementChecker_.checkConstraints(state);
        vertexChecker_.checkConstraints(state);

        // Check entry action
        if (null != state.getEntryBehavior()) {
            state.getEntryBehavior().acceptConstraintVisitor(this);
        }

        // Check exit action
        if (null != state.getExitBehavior()) {
            state.getExitBehavior().acceptConstraintVisitor(this);
        }

        // check triggers for deferrable events
        Set<ITrigger> triggers = state.getDeferrableTriggers();
        if (null != triggers) {
            for (ITrigger trigger : triggers) {
                trigger.acceptConstraintVisitor(this);
            }
        }
    }

    public ITriggerChecker getTriggerChecker() {
        return triggerChecker_;
    }

    public void setTriggerChecker(ITriggerChecker triggerChecker) {
        this.triggerChecker_ = triggerChecker;
    }

    public void visitTrigger(ITrigger trigger) {
        triggerChecker_.checkConstraints(trigger);
        namedElementChecker_.checkConstraints(trigger);

        // Check the associated event
        trigger.getEvent().acceptConstraintVisitor(this);
    }

    public IEventChecker getEventChecker() {
        return eventChecker_;
    }

    public void setEventChecker(IEventChecker eventChecker) {
        this.eventChecker_ = eventChecker;
    }

    public void visitEvent(IEvent event) {
        eventChecker_.checkConstraints(event);
        namedElementChecker_.checkConstraints(event);
    }

    public IVertexChecker getVertexChecker() {
        return vertexChecker_;
    }

    public void setVertexChecker(IVertexChecker vertexChecker) {
        this.vertexChecker_ = vertexChecker;
    }
}
