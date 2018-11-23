package org.suggs.fsm.engine.uml2.behaviorstatemachines;

import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;
import org.suggs.fsm.uml2.scribe.optimisation.ITransitionFactory;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.suggs.fsm.uml2.behaviorstatemachines.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Implements shallow and deep history state functionality in addition
 * to standard pseusoState behaviour.
 */
public class HistoryState extends PseudoState implements IHistoryState {

    private static final Log LOG = LogFactory.getLog(HistoryState.class);

    /**
     * The default constructor.
     *
     * @param pseudostateKind The type of pseudostate.
     */
    public HistoryState(String pseudostateKind) {
        super(pseudostateKind);
    }

    public void stateEntered(IState state, IStateMachineContext stateMachineContext) {
        LOG.debug("StateEntered: [" + state.getName() + "]");
        stateMachineContext.getHistoryStates().put(getQualifiedName(), state.getQualifiedName());
    }

    public void acceptOptimiser(IModelOptimiser modelOptimiser) {

        pseudoStateBehaviour_.acceptOptimiser(modelOptimiser);

        // If no default transition has been set, add an outgoing
        // transition to the initial pseudostate for
        // the containing region
        if (0 == getOutgoing().size()) {
            ITransitionFactory transitionFactory = modelOptimiser.getTransitionFactory();
            IVertex initialPseudostate = getContainer().getInitialState();
            addOutgoingTransition(transitionFactory.createTransition(ITransition.EXTERNAL, this, initialPseudostate));
        }

        // Finalise by registering as a state entry listener with the
        // containing region, recursing if this is a deep history
        getContainer().addStateEntryListener((PseudoState.DEEP_HISTORY.equals(getPseudoStateKind()) ? true : false), this);

    }

    public void setPseudoStateKind(String pseudostateKind) {

        /*
         * Set the behaviour based on the kind of pseudostate
         * specified.
         */
        if (IPseudoState.DEEP_HISTORY.equals(pseudostateKind)) {
            pseudoStateBehaviour_ = new DeepHistoryPseudoStateBehaviour();
        } else if (IPseudoState.SHALLOW_HISTORY.equals(pseudostateKind)) {
            pseudoStateBehaviour_ = new ShallowHistoryPseudoStateBehaviour();
        } else {
            String msg = "Invalid pseudostate kind specified: " + pseudostateKind;
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

    }

    /**
     * Encapsulates the behavioural aspects of a DEEP_HISTORY pseudostate.
     */
    private class DeepHistoryPseudoStateBehaviour implements IPseudoStateBehaviour {

        public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            transitionToHistoryState(eventContext, namespaceContext, stateMachineContext);
        }

        public void exit(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            // History states have no exit behavior
        }

        public String getPseudostateKind() {
            return IPseudoState.DEEP_HISTORY;
        }

        public boolean isInitialPseudostate() {
            return false;
        }

        public void acceptOptimiser(IModelOptimiser modelOptimiser) {
            // History pseudostates do not inherit transitions from
            // enclosing states
            prioritisedOutgoingTransitions_ = new ArrayList();
            prioritisedOutgoingTransitions_.add(getOutgoing());
        }

        public List getAllPossibleOutgoingTransitions() {
            // History states do not inherit transitions from
            // enclosing states

            List outgoingTransitions = new ArrayList();
            outgoingTransitions.add(getOutgoing());

            return outgoingTransitions;
        }
    }

    /**
     * Encapsulates the behavioural aspects of a SHALLOW_HISTORY
     * pseudostate.
     */
    private class ShallowHistoryPseudoStateBehaviour implements IPseudoStateBehaviour {

        public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            transitionToHistoryState(eventContext, namespaceContext, stateMachineContext);
        }

        public void exit(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            // History states have no exit behavior
        }

        public String getPseudostateKind() {
            return IPseudoState.SHALLOW_HISTORY;
        }

        public boolean isInitialPseudostate() {
            return false;
        }

        public void acceptOptimiser(IModelOptimiser modelOptimiser) {

            // History pseudostates do not inherit transitions from
            // enclosing states
            prioritisedOutgoingTransitions_ = new ArrayList();
            prioritisedOutgoingTransitions_.add(getOutgoing());

        }

        public List getAllPossibleOutgoingTransitions() {
            // History states do not inherit transitions from
            // enclosing states

            List outgoingTransitions = new ArrayList();
            outgoingTransitions.add(getOutgoing());

            return outgoingTransitions;

        }
    }

    /**
     * Gets the default transition out of this history state. This
     * should be a transition to the initial pseudostate of the
     * containing region.
     *
     * @return The default transition out of this history state.
     */
    protected ITransition getDefaultTransition() {

        Set transitions = getOutgoing();

        if (transitions.size() != 1) {

            String msg = "History state " + this + " has " + transitions.size() + " outgoing transitions";
            LOG.error(msg);
            throw new RuntimeException(msg);

        } else {
            Iterator iter = transitions.iterator();
            return (ITransition) iter.next();
        }

    }

    protected void transitionToHistoryState(IEventContext eventContext, INamespaceContext namespaceContext,
                                            IStateMachineContext stateMachineContext) {

        String targetQualifiedName = (String) stateMachineContext.getHistoryStates().get(getQualifiedName());

        if (null == targetQualifiedName) {

            /*
             * No history state mapping held for this state, so follow
             * the default transition.
             */

            getDefaultTransition().fire(eventContext, namespaceContext, stateMachineContext);

        } else {

            IState targetState = (IState) namespaceContext.getNamespaceObjectManager().getObject(targetQualifiedName);

            // Clone the default transition and change the target
            // state
            ITransition transition = getDefaultTransition().createShallowCopy();
            transition.setOutgoingVertex(targetState);
            transition.fire(eventContext, namespaceContext, stateMachineContext);

        }

    }
}
