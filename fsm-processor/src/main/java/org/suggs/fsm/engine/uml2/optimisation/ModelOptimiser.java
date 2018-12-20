package org.suggs.fsm.engine.uml2.optimisation;

import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState;
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;
import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;
import org.suggs.fsm.uml2.scribe.optimisation.ITransitionFactory;

public class ModelOptimiser implements IModelOptimiser {

    /**
     * The factory used when generating transitionBuilders as part of the
     * optimisation process. For example, transitionBuilders are generated
     * from enclosed states.
     */
    private ITransitionFactory transitionFactory_;

    public void optimiseRegion(IRegion region) {
        region.acceptOptimiser(this);
    }

    public void optimiseSimpleState(IState state) {
        state.acceptOptimiser(this);
    }

    public void optimiseSimpleCompositeState(IState state) {
        state.acceptOptimiser(this);
    }

    public void optimiseStateMachine(IStateMachine stateMachine) {
        stateMachine.acceptOptimiser(this);
    }

    public void optimiseEntryPointPseudoState(IPseudoState pseudoState) {
        pseudoState.acceptOptimiser(this);
    }

    public void optimiseDeepHistoryState(IPseudoState pseudoState) {
        pseudoState.acceptOptimiser(this);
    }

    public void optimiseExitPointPseudoState(IPseudoState pseudoState) {
        pseudoState.acceptOptimiser(this);
    }

    public void optimiseInitialPseudoState(IPseudoState pseudoState) {
        pseudoState.acceptOptimiser(this);
    }

    public void optimiseShallowHistoryState(IPseudoState pseudoState) {
        pseudoState.acceptOptimiser(this);
    }

    public void optimiseBehavioredClassifier(IBehavioredClassifier behavioredClassifier) {
        behavioredClassifier.acceptOptimiser(this);
    }

    public void optimiseBehavior(IBehavior behavior) {
        behavior.acceptOptimiser(this);
    }

    public ITransitionFactory getTransitionFactory() {
        return transitionFactory_;
    }

    public void setTransitionFactory(ITransitionFactory transitionFactory) {
        this.transitionFactory_ = transitionFactory;
    }
}
