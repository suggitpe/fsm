package org.suggs.fsm.engine.uml2.behaviorstatemachines;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;
import org.suggs.fsm.uml2.scribe.optimisation.IOptimisable;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Implemenation of the UML PseudoState class as per UML Modeling
 * Language: Superstructure Version 2.0 formal/05-07-04. Pseudostates
 * include initial states and entry and exit points. History states
 * are implemented using the subclass
 */
public class PseudoState extends Vertex implements IPseudoState {

    private static final Logger LOG = LoggerFactory.getLogger(PseudoState.class);

    /**
     * The behavior provider for this pseudostate.
     */
    protected IPseudoStateBehaviour pseudoStateBehaviour_;

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        constraintVisitor.visitPseudoState(this);
    }

    /**
     * The default constructor.
     *
     * @param pseudostateKind The type of pseudostate.
     */
    public PseudoState(String pseudostateKind) {
        super();
        setPseudoStateKind(pseudostateKind);
    }

    public String getPseudoStateKind() {
        return pseudoStateBehaviour_.getPseudostateKind();
    }

    public void setPseudoStateKind(String pseudostateKind) {

        /*
         * Set the behaviour based on the kind of pseudostate
         * specified.
         */
        if (IPseudoState.INITIAL.equals(pseudostateKind)) {
            pseudoStateBehaviour_ = new InitialPseudoStateBehaviour();

        } else if (IPseudoState.ENTRY_POINT.equals(pseudostateKind)) {
            pseudoStateBehaviour_ = new EntryPointPseudoStateBehaviour();

        } else if (IPseudoState.EXIT_POINT.equals(pseudostateKind)) {
            pseudoStateBehaviour_ = new ExitPointPseudoStateBehaviour();

        } else {
            String msg = "Invalid pseudostate kind specified: " + pseudostateKind;
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

    }

    public boolean isInitialPseudostate() {
        return pseudoStateBehaviour_.isInitialPseudostate();
    }

    /**
     * Defines the pseudostate behaviour that is dependent on the kind
     * of pseudostate
     */
    protected interface IPseudoStateBehaviour extends IOptimisable {
        String getPseudostateKind();
        boolean isInitialPseudostate();
        void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext);
        void exit(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext);
        List getAllPossibleOutgoingTransitions();
    }

    /**
     * Encapsulates the behavioural aspects of an INITIAL pseudostate.
     */
    private class InitialPseudoStateBehaviour implements IPseudoStateBehaviour {

        public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {

            // Follow the initial transitions
            Set<ITransition> transitions = getOutgoing();

            if (1 != transitions.size()) {
                String msg = "Initial PseudoState " + PseudoState.this + " has " + transitions.size() + " outgoing transitions";
                LOG.error(msg);
                throw new RuntimeException(msg);
            } else {
                Iterator<ITransition> iter = transitions.iterator();
                (iter.next()).fire(eventContext, namespaceContext, stateMachineContext);
            }

        }

        public void exit(IEventContext event, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            // No exit behaviour
        }

        public String getPseudostateKind() {
            return IPseudoState.INITIAL;
        }

        public boolean isInitialPseudostate() {
            return true;
        }

        public void acceptOptimiser(IModelOptimiser modelOptimiser) {
            // Initial pseudostates do not inherit transitions from
            // enclosing states
            prioritisedOutgoingTransitions_ = new ArrayList();
            prioritisedOutgoingTransitions_.add(getOutgoing());
        }

        public List getAllPossibleOutgoingTransitions() {
            // Initial pseudostates do not inherit transitions from
            // enclosing states

            List outgoingTransitions = new ArrayList();
            outgoingTransitions.add(getOutgoing());

            return outgoingTransitions;
        }
    }

    /**
     * Encapsulates the behavioural aspects of an ENTRY_POINT
     * pseudostate.
     */
    private class EntryPointPseudoStateBehaviour implements IPseudoStateBehaviour {

        public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {

            /*
             * Entering an entry pseudostate causes an automatic
             * transition to the initial pseudostate of the region
             * region.
             */

            Set transitions = getOutgoing();

            if (1 != transitions.size()) {
                String msg = transitions.size() + " outgoing transitions were found from " + PseudoState.this + ": " + transitions;
                LOG.error(msg);
                throw new RuntimeException(msg);
            } else {
                Iterator iter = transitions.iterator();
                ((ITransition) iter.next()).fire(eventContext, namespaceContext, stateMachineContext);
            }

        }

        public void exit(IEventContext event, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            // No exit behaviour
        }

        public String getPseudostateKind() {
            return IPseudoState.ENTRY_POINT;
        }

        public boolean isInitialPseudostate() {
            return false;
        }

        public void acceptOptimiser(IModelOptimiser modelOptimiser) {

            // Entry pseudostates do not inherit transitions from
            // enclosing states
            prioritisedOutgoingTransitions_ = new ArrayList();
            prioritisedOutgoingTransitions_.add(getOutgoing());

            // Add an outbound transition to the initial pseudostate
            // for the compound state that contains this pseudostate

            Transition transition = new Transition(ITransition.INTERNAL);
            transition.setIncomingVertex(PseudoState.this);
            transition.setOutgoingVertex(PseudoState.this.getContainer().getInitialState());
            transition.setName("Default entry transition");

            PseudoState.this.addOutgoingTransition(transition);
        }

        public List<ITransition> getAllPossibleOutgoingTransitions() {
            return prioritisedOutgoingTransitions_;
        }
    }

    /**
     * Encapsulates the behavioural aspects of an EXIT_POINT
     * pseudostate.
     */
    private class ExitPointPseudoStateBehaviour implements IPseudoStateBehaviour {

        public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {

        }

        public void exit(IEventContext event, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            // No action
        }

        public String getPseudostateKind() {
            return IPseudoState.EXIT_POINT;
        }

        public boolean isInitialPseudostate() {
            return false;
        }

        public void acceptOptimiser(IModelOptimiser modelOptimiser) {

            // Exit pseudostates do not inherit transitions from
            // enclosing states
            prioritisedOutgoingTransitions_ = new ArrayList();
            prioritisedOutgoingTransitions_.add(getOutgoing());
        }

        public List getAllPossibleOutgoingTransitions() {
            // Exit pseudostates do inherit transitions from enclosing
            // states

            List<Set<ITransition>> outgoingTransitions = new ArrayList<>();
            outgoingTransitions.add(getOutgoing());

            IState enclosingState = getContainer().getState().getContainer().getState();

            if (null != enclosingState) {
                // The compound state that owns this exit point is
                // enclosed in another state
                outgoingTransitions.addAll(enclosingState.getAllPossibleOutgoingTransitions());
            }

            return outgoingTransitions;
        }
    }

    public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
        pseudoStateBehaviour_.enter(eventContext, namespaceContext, stateMachineContext);
    }

    public void exit(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
        pseudoStateBehaviour_.exit(eventContext, namespaceContext, stateMachineContext);
    }

    public void acceptOptimiser(IModelOptimiser modelOptimiser) {
        pseudoStateBehaviour_.acceptOptimiser(modelOptimiser);
    }

    public void doEntryAction(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
        // Pseudostates have no entry action
    }

    public void doExitAction(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
        // Pseudostates have no exit action
    }

    public List getAllPossibleOutgoingTransitions() {
        return pseudoStateBehaviour_.getAllPossibleOutgoingTransitions();
    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    @Override
    public String toString() {
        return "PseudoState{" +
                "pseudoStateBehaviour_=" + pseudoStateBehaviour_ +
                '}';
    }
}
