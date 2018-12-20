package org.suggs.fsm.uml2.scribe.optimisation;

import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState;
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;
import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine;

/**
 * Implementations of this interface visit state model components and
 * perform transformations required to optimise the model for runtime.
 */
public interface IModelOptimiser {

    /**
     * Optimises a state machine.
     */
   void optimiseStateMachine(IStateMachine stateMachine);

    /**
     * Optimises a region of a state machine or state.
     */
    void optimiseRegion(IRegion region);

    /**
     * Optimises a simple state.
     */
    void optimiseSimpleState(IState state);

    /**
     * Optimises a simple composite state.
     */
    void optimiseSimpleCompositeState(IState state);

    /**
     * Optimises an entry point pseudostate.
     */
    void optimiseEntryPointPseudoState(IPseudoState pseudoState);

    /**
     * Optimises an exit point pseudostate.
     */
    void optimiseExitPointPseudoState(IPseudoState pseudoState);

    /**
     * Optimises an pseudoStateKind pseudostate.
     */
    void optimiseInitialPseudoState(IPseudoState pseudoState);

    /**
     * Optimises a deep history point pseudostate.
     */
    void optimiseDeepHistoryState(IPseudoState pseudoState);

    /**
     * Optimises a shallow history pseudostate.
     */
    void optimiseShallowHistoryState(IPseudoState pseudoState);

    /**
     * Optimises a behavior.
     */
    void optimiseBehavior(IBehavior behavior);

    /**
     * Optimises a behaviored classifier.
     */
    void optimiseBehavioredClassifier(IBehavioredClassifier behavioredClassifier);

    /**
     * Gets the transition factory that will be used to create new
     * transitionBuilders as part of the optimisation process.
     */
    ITransitionFactory getTransitionFactory();

    /**
     * Sets a transition factory to be used to create new transitionBuilders
     * as part of the optimisation process. <p/>Additional transitionBuilders
     * are added to the state machine object model to improve runtime
     * performance. For example, enclosed states inherit the outgoing
     * transitionBuilders from their enclosing state but these are not
     * expressed explicitly in a UML state model. The optimisation
     * process adds these transitionBuilders explicitly to remove the need
     * for runtime checking.
     */
    void setTransitionFactory(ITransitionFactory transitionFactory);

}
