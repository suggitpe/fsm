package org.suggs.fsm.engine.uml2.behaviorstatemachines;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.engine.uml2.kernel.Namespace;
import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState;
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.communications.ITrigger;
import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.kernel.INamespace;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;
import org.suggs.fsm.uml2.scribe.optimisation.IOptimisable;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateEntryListener;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

import java.util.*;

public class State extends Vertex implements IState {

    private static final Logger LOG = LoggerFactory.getLogger(State.class);

    private INamespace namespaceFacet;
    private IBehavior entryBehavior;
    private IBehavior exitBehavior;
    private IRegion region;
    private Set<ITrigger> deferrableTriggers = new HashSet<>();
    private IStateBehaviour stateBehaviour;
    /**
     * Holds an ordered list of objects that have registered to receive notfications when this state is
     * entered as part of a transition.
     */
    private List<IStateEntryListener> stateEntryListeners_ = new ArrayList<>();

    private State() {
        namespaceFacet = new Namespace();
    }

    public State(String stateKind) {
        setStateKind(stateKind);
    }

    /**
     * Sets the kind of this state, which determines is behavior and structure.
     */
    protected void setStateKind(String stateKind) {
        if (IState.SIMPLE.equals(stateKind)) {
            stateBehaviour = new SimpleStateBehaviour();
        } else if (IState.SIMPLE_COMPOSITE.equals(stateKind)) {
            stateBehaviour = new SimpleCompositeStateBehaviour();
        } else if (IState.TRANSIENT.equals(stateKind)) {
            stateBehaviour = new TransientStateBehaviour();
        } else {
            throw new RuntimeException("State kind " + stateKind + " is not supported.");
        }
    }

    public String getStateKind() {
        return stateBehaviour.getStateKind();
    }

    public void setEntryBehavior(IBehavior behavior) {
        entryBehavior = behavior;
    }

    public IBehavior getEntryBehavior() {
        return entryBehavior;
    }

    public void setExitBehavior(IBehavior behavior) {
        exitBehavior = behavior;
    }

    public IBehavior getExitBehavior() {
        return exitBehavior;
    }

    public boolean isComposite() {
        return stateBehaviour.isComposite();
    }

    public boolean isSimple() {
        return stateBehaviour.isSimple();
    }

    public boolean isTransient() {
        return stateBehaviour.isTransient();
    }

    public boolean isSubmachineState() {
        return stateBehaviour.isSubmachineState();
    }

    public Set<INamedElement> getOwnedMembers() {
        return namespaceFacet.getOwnedMembers();
    }

    public void setOwnedMembers(Set<INamedElement> ownedMembers) {
        namespaceFacet.setOwnedMembers(ownedMembers);
    }

    public void addOwnedMember(INamedElement ownedMember) {
        namespaceFacet.addOwnedMember(ownedMember);
    }

    public IRegion getRegion() {
        return region;
    }

    public void setRegion(IRegion region) {
        stateBehaviour.setRegion(region);
    }

    public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
        stateBehaviour.enter(eventContext, namespaceContext, stateMachineContext);
        notifyStateEntryListeners(stateMachineContext);
    }

    /**
     * Notifies any state entry listeners that this state has been entered.
     */
    protected void notifyStateEntryListeners(IStateMachineContext stateMachineContext) {
        for (IStateEntryListener listener : stateEntryListeners_) {
            listener.stateEntered(this, stateMachineContext);
        }
    }

    public void exit(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
        stateBehaviour.exit(eventContext, namespaceContext, stateMachineContext);
    }

    public void acceptNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        super.acceptNamespaceObjectManager(namespaceObjectManager);
        namespaceObjectManager.visitState(this);
    }

    /**
     * Iterates through a List of Sets of transions and checks whether there are any transitions present.
     *
     * @return <code>true</code> if any of the Sets in the List have non-zero size, <code>false</code> otherwise.
     */
    protected boolean hasTransitions(List<Set<ITransition>> prioritisedTransitions) {

        // If there are no possible transitions for this event
        boolean potentialTransitionsExist = false;
        for (Set<ITransition> transitionSet : prioritisedTransitions) {
            if (0 < transitionSet.size()) {
                potentialTransitionsExist = true;
            }
        }
        return potentialTransitionsExist;
    }

    public void processEvent(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {

        try {
            // find a firable transition for this event
            ITransition firableTransition = getEnabledTransition(eventContext, namespaceContext, stateMachineContext);

            // transition found, fire it.
            if (null != firableTransition) {
                firableTransition.fire(eventContext, namespaceContext, stateMachineContext);
            }

            // firableTransition will be null if there are valid
            // transitions
            // for the event, but all the guards failed.
            // Queue the event if can defer from this state.
            else if (defersEvent(eventContext.getEvent())) {
                stateMachineContext.getStateManager().getDeferredEvents().add(eventContext);
                stateMachineContext.getEventInterceptor().onEventDeferred(eventContext);
            }

            // discard and notify listners otherwise
            else {
                LOG.debug("Event " + eventContext.getEvent().toString() + " was skipped for state " + this.toString());
                stateMachineContext.getEventInterceptor().onEventSkipped(eventContext, namespaceContext, stateMachineContext);
            }
        } catch (UnprocessableEventException e) {   // no transitions were found, if the event is deferrable
            // from this state, then this is a valid scenario. 
            // Queue the event in state manager for later.
            if (defersEvent(eventContext.getEvent())) {
                stateMachineContext.getStateManager().getDeferredEvents().add(eventContext);
                stateMachineContext.getEventInterceptor().onEventDeferred(eventContext);
            } else {
                throw e;
            }
        }
    }

    /**
     * Returns the enabled transition for this state given the event context. A transition is considered to
     * be enabled if the event specified by its trigger matches that of the event context, AND the guard
     * (if any) evaulates to true.
     */
    public ITransition getEnabledTransition(IEventContext eventContext, INamespaceContext namespaceContext,
                                            IStateMachineContext stateMachineContext) {

        List<Set<ITransition>> potentialOutgoingTransitions = getAllPossibleOutgoingTransitions(eventContext.getEvent().getType());

        if (!hasTransitions(potentialOutgoingTransitions)) {
            StringBuffer msg = new StringBuffer();
            msg.append("No potential transitions for event [")
                    .append(eventContext.getEvent().getType()).append("], in state [")
                    .append(this.getName()).append("]");

            LOG.info(msg.toString());
            throw new UnprocessableEventException(msg.toString());
        } else {
            /*
             * Iterate over the transitions at each priority level, starting at the highest priority. */
            for (Set<ITransition> prioritySet : potentialOutgoingTransitions) {

                // Examine the potential transitions at each priority
                // level
                if (0 < prioritySet.size()) {

                    Set<ITransition> enabledTransitions = new HashSet<>();
                    for (ITransition transition : prioritySet) {
                        if (transition.isEnabled(eventContext, namespaceContext, stateMachineContext)) {
                            // Guard passed
                            enabledTransitions.add(transition);
                        }
                    }

                    if (0 < enabledTransitions.size()) {
                        if (1 == enabledTransitions.size()) {
                            return (enabledTransitions.iterator().next());
                        } else {
                            StringBuffer msg = new StringBuffer();
                            msg.append("Conflicting transitions were enabled for event [")
                                    .append(eventContext.getEvent().getType()).append("], in state [")
                                    .append(this.getName()).append("], transitions [");

                            // we should be guaranteed at least 2 to have
                            // reached here
                            Iterator<ITransition> ei = enabledTransitions.iterator();
                            ITransition transition = ei.next();
                            msg.append(transition.getName());

                            while (ei.hasNext()) {
                                transition = ei.next();
                                msg.append(",").append(transition.getName());
                            }
                            msg.append("]");

                            LOG.error(msg.toString());
                            throw new ConflictingTransitionsException(msg.toString());
                        }
                    }
                }
            }
        }
        /*
         * To have reached here, potential transitions were found but
         * none can fire because all guards failed.
         */
        return null;
    }

    public void doEntryAction(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
        if (null != getEntryBehavior()) {
            getEntryBehavior().execute(eventContext, namespaceContext, stateMachineContext);
        }
    }

    public void doExitAction(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
        if (null != getExitBehavior()) {
            getExitBehavior().execute(eventContext, namespaceContext, stateMachineContext);
        }

    }

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        super.acceptConstraintVisitor(constraintVisitor);
        constraintVisitor.visitState(this);
    }

    public Set<ITrigger> getAllPossibleDeferrableTriggers() {
        // initialise with our own defer list
        Set<ITrigger> allTriggers = new HashSet<>(getDeferrableTriggers());

        // add the ones from our enclosing states
        if (getContainer().getState() != null) {
            allTriggers.addAll(getContainer().getState().getAllPossibleDeferrableTriggers());
        }

        return allTriggers;
    }

    public void acceptOptimiser(IModelOptimiser modelOptimiser) {

        // Set up the prioritised list of all possible outgoing
        // transitions

        prioritisedOutgoingTransitions_ = new ArrayList<>();

        prioritisedOutgoingTransitions_.add(0, State.super.getOutgoing());

        // Stores any new extended transitions that are created
        Set<ITransition> newTransitions = new HashSet<>();

        if (null != getContainer().getState()) {

            List<Set<ITransition>> inheritedTransitions = getContainer().getState().getAllPossibleOutgoingTransitions();

            for (int i = 0; i < inheritedTransitions.size(); i++) {
                Set<ITransition> priorityLevelSet = new HashSet<>();
                Set<ITransition> s = inheritedTransitions.get(i);
                for (ITransition transition : s) {
                    ITransition extendedTransition = transition.createShallowCopy();
                    extendedTransition.setIncomingVertex(this);
                    priorityLevelSet.add(extendedTransition);
                    priorityLevelSet.add(transition);
                }
                prioritisedOutgoingTransitions_.add(i + 1, priorityLevelSet);
            }

            // add any deferred triggers from enclosing states to our
            // set
            deferrableTriggers.addAll(getContainer().getState().getAllPossibleDeferrableTriggers());
        }

        /*
         * Optimise the new extended transitions. This is done outside
         * of the inherited transitions iteration since it modifies
         * the source and target vertives by adding the transitio to
         * them and would otherwise give a
         * ConcurrentModificationException.
         */

        for (ITransition transition : newTransitions) {
            transition.acceptOptimiser(modelOptimiser);
        }

        // Do optimisation for the new transitions and state kind.
        stateBehaviour.acceptOptimiser(modelOptimiser);

    }

    /**
     * Sends a completion event to this state.
     *
     * @return <code>true</code> if the completion event fired a transition, <code>false</code> otherwise.
     */
    protected boolean fireCompletionEvent(IEventContext eventContext, INamespaceContext namespaceContext,
                                          IStateMachineContext stateMachineContext) {
        return fireInternalEvent(
                org.suggs.fsm.uml2.communications.IEvent.COMPLETION_EVENT_QUALIFIED_NAME,
                eventContext,
                namespaceContext,
                stateMachineContext);
    }

    /**
     * Sends an internal event to this state. An event is considered to be
     * internal if it was created by an action within the FSM, rather than
     * from an external source.
     *
     * @return <code>true</code> if the event was able to fire a transition, <code>false</code> otherwise.
     */
    protected boolean fireInternalEvent(String eventType,
                                        IEventContext eventContext,
                                        INamespaceContext namespaceContext,
                                        IStateMachineContext stateMachineContext) {
        org.suggs.fsm.event.IEvent newEvent =
                stateMachineContext.getEventFactory().createEvent(
                        eventType, eventContext.getEvent());

        newEvent.setBusinessObject(eventContext.getEvent().getBusinessObject());

        eventContext.setEvent(newEvent);
        try {
            LOG.debug("Firing Internal event: [" + eventContext.getEvent().getType() + "]");

            processEvent(eventContext, namespaceContext, stateMachineContext);
            return true;
        } catch (UnprocessableEventException e) {
            LOG.debug("Internal event skipped: [" + eventContext.getEvent().getType() + "]");
            ; // OK to silently skip completion events
        }

        return false;
    }

    /**
     * Objects that implement this interface provide behavioral functioality to state objects.
     */
    private interface IStateBehaviour extends IOptimisable {

        /**
         * Sets the region that this state behavior owns. This is only relevant for composite states, which
         * own a region that contains their substates.
         */
        void setRegion(IRegion region);

        /**
         * @return <code>true</code> if the behavior is that of a composite state.
         */
        boolean isComposite();

        /**
         * @return <code>true</code> if the behavior is that of a simple state.
         */
        boolean isSimple();

        /**
         * @return <code>true</code> if the behavior is that of a transient simple state.
         */
        boolean isTransient();

        /**
         * @return <code>true</code> if the behavior is that of a submachine state.
         */
        boolean isSubmachineState();

        /**
         * Adds a state entry listener to this state behavior. The effect of adding a state entry listener
         * is dependent on the kind of state. Coposite states should cascade the addition of a deep history
         * state entry listner to their enclosed states.
         *
         * @param recurse  <code>true</code> if the listener should also be added to any enclosed states.
         */
        void addStateEntryListener(boolean recurse, IStateEntryListener listener);

        String getStateKind();

        /**
         * Adds an incoming transition to the state. The effect of adding a transition depends on the kind of
         * state, with composite states attaching the transition to their entry point.
         */
        void addIncomingTransition(ITransition transition);

        /**
         * Adds an outgoing transition to the state. The effect of adding a transition depends on the kind of
         * state, with composite states attaching the transition to their exit point.
         */
        void addOutgoingTransition(ITransition transition);

        /**
         * Gets all possible transitions out of this state. Transitions are returned as a list of sets. The
         * list is ordered in priority with the highest priority transitions in the set with index 0. The
         * highest priority transitions are those defined in the state model as leaving the state
         * directly. Transitions defined in the model as leaving the immediate enclosing state are returned
         * as a set at index 1 in the list and so on for any more enclosing states.
         *
         * @return All possible transitions out of this state.
         */
        List<Set<ITransition>> getAllPossibleOutgoingTransitions();

        void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext);

        void exit(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext);

    }

    /**
     * Encapsulates state behavior for simple, non-composite, states.
     */
    private class SimpleStateBehaviour implements IStateBehaviour {

        public String getStateKind() {
            return IState.SIMPLE;
        }

        public boolean isComposite() {
            return false;
        }

        public boolean isSimple() {
            return true;
        }

        public boolean isTransient() {
            return false;
        }

        public boolean isSubmachineState() {
            return false;
        }

        public void setRegion(IRegion regions) {
            throw new RuntimeException("Simple States cannot have regions");
        }

        public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            // Update persistent state.
            stateMachineContext.getStateManager().storeActiveState(getQualifiedName());

            // 1. get current deferred event list from state manager.
            // 2. keep a local copy of this list.
            // 3. remove from the state manager, all deferred events
            //    that are not deferrable in this state.
            // 4. Send completion event to self, this may trigger an
            //    automatic transition out of this state. This is why we need
            //    to clean up the state manager deferred list before
            //    sending the completion event. (we don't want the new state to
            //    see deferred events that this state should have discarded).

            List<IEventContext> managerDeferList = stateMachineContext.getStateManager().getDeferredEvents();

            // do initialisation if the state manager hasn't done so.
            if (managerDeferList == null) {
                managerDeferList = new ArrayList<>();
                stateMachineContext.getStateManager().storeDeferredEvents(managerDeferList);
            }

            // our own local copy so that code can be used
            List<IEventContext> deferredEvents = new ArrayList(managerDeferList);

            // remove all non deferrable events from state manager
            // list
            for (Iterator<IEventContext> iter = managerDeferList.iterator(); iter.hasNext(); ) {
                if (!defersEvent(iter.next().getEvent())) {
                    iter.remove();
                }
            }

            /*
             * Send a completion event to self. This may trigger a
             * completion transition - fireCompletionEvent() will
             * return true in this case. If we don't transition out,
             * then we check the original list of deferred events to
             * see if any can fire.
             */
            if (!fireCompletionEvent(eventContext, namespaceContext, stateMachineContext)) {
                String internalEvent = eventContext.getInternalEvent();
                boolean internalEventFired = false;

                // The entry action in this state might have created an 
                // internal event. Check this and try to fire a transition
                // with the internal event on self.
                if (null != internalEvent) {
                    eventContext.removeInternalEvent();
                    internalEventFired = fireInternalEvent(internalEvent,
                            eventContext, namespaceContext, stateMachineContext);
                }

                if (!internalEventFired) {
                    ITransition transitionToFire = null;
                    IEventContext deferredEventContextToFire = null;

                    // search for first deferred event that can fire,
                    // discard all others that are no longer deferrable
                    // from this state.
                    for (IEventContext context : deferredEvents) {
                        if (null == transitionToFire) {
                            try {
                                transitionToFire = getEnabledTransition(context, namespaceContext, stateMachineContext);

                                if (null != transitionToFire) {
                                    deferredEventContextToFire = context;
                                } else if (null == transitionToFire && !defersEvent(context.getEvent())) {
                                    discardDeferredEvent(context, stateMachineContext);
                                }
                            } catch (UnprocessableEventException e) {
                                if (!defersEvent(context.getEvent())) {
                                    discardDeferredEvent(context, stateMachineContext);
                                }
                                // ok for deferred event to not have valid
                                // transition out
                            }
                        } else if (!defersEvent(context.getEvent())) {
                            discardDeferredEvent(context, stateMachineContext);
                        }
                    }

                    // found a deferred event that can trigger transition,
                    // fire it
                    if (null != transitionToFire) {
                        stateMachineContext.getStateManager().getDeferredEvents().remove(deferredEventContextToFire);

                        transitionToFire.fire(deferredEventContextToFire, namespaceContext, stateMachineContext);
                    }
                }
            }
        }

        public void exit(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            /*
             * Update the current state with a temporary value, since
             * we're really between states. Under normal operation,
             * the value set should never be written back
             */
            stateMachineContext.getStateManager().storeActiveState(IState.TRANSITIONING);
        }

        public void acceptOptimiser(IModelOptimiser modelOptimiser) {

            // No optimisation/ finalisation

        }

        public void addStateEntryListener(boolean recurse, IStateEntryListener listener) {
            stateEntryListeners_.add(listener);
        }

        public void addIncomingTransition(ITransition transition) {
            State.super.addIncomingTransition(transition);
        }

        public void addOutgoingTransition(ITransition transition) {
            State.super.addOutgoingTransition(transition);
        }

        public List<Set<ITransition>> getAllPossibleOutgoingTransitions() {
            return State.super.getAllPossibleOutgoingTransitions();
        }

        /**
         * Notifies listeners that a deferred event has been
         * discarded.
         */
        private void discardDeferredEvent(IEventContext context, IStateMachineContext stateMachineContext) {
            LOG.debug("Deferred Event " + context.getEvent().toString() + " was discarded for state " + this.toString());
            stateMachineContext.getEventInterceptor().onDeferredEventDiscarded(context);
        }
    }


    /**
     * Encapsulates state behavior for transient states. Transients are
     * a specialisation of simple states, except we should never end up in
     * a transient state. An FSM should enter a transient state, and then
     * immediately transition out again via an event generated by its entry
     * action, or otherwise. It is an error to end up in a transient state.
     */
    private class TransientStateBehaviour extends SimpleStateBehaviour {
        public String getStateKind() {
            return IState.TRANSIENT;
        }

        public boolean isTransient() {
            return true;
        }

    } // End class TransientStateBehaviour


    /**
     * Encapsulates state behavior for composite non-orthogonal states.
     */
    private class SimpleCompositeStateBehaviour implements IStateBehaviour {

        private IPseudoState entryPoint_;

        private IPseudoState exitPoint_;

        public SimpleCompositeStateBehaviour() {
            entryPoint_ = new PseudoState(PseudoState.ENTRY_POINT);
            exitPoint_ = new PseudoState(PseudoState.EXIT_POINT);

            entryPoint_.setName(IPseudoState.ENTRY_POINT);
            exitPoint_.setName(IPseudoState.EXIT_POINT);
        }

        public String getStateKind() {
            return IState.SIMPLE_COMPOSITE;
        }

        public boolean isComposite() {
            return true;
        }

        public boolean isSimple() {
            return false;
        }

        public boolean isTransient() {
            return false;
        }

        public boolean isSubmachineState() {
            return false;
        }

        public void setRegion(IRegion region) {

            State.this.region = region;

            // Associate the region with this state machine
            getRegion().setState(State.this);

            // Add entry and exit pseudostates with
            getRegion().addSubVertex(entryPoint_);

            getRegion().addSubVertex(exitPoint_);

        }

        public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            // Update persistent state.
            stateMachineContext.getStateManager().storeActiveState(getQualifiedName());
            entryPoint_.enter(eventContext, namespaceContext, stateMachineContext);
        }

        public void exit(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
            // Update persistent state
            stateMachineContext.getStateManager().storeActiveState(null);
        }

        public void acceptOptimiser(IModelOptimiser modelOptimiser) {
            region.acceptOptimiser(modelOptimiser);
        }

        public void addStateEntryListener(boolean recurse, IStateEntryListener listener) {
            stateEntryListeners_.add(listener);
            if (recurse) {
                getRegion().addStateEntryListener(recurse, listener);
            }
        }

        public void addIncomingTransition(ITransition transition) {
            // Map the transaction endpoint to the Entry pseudostate
            entryPoint_.addIncomingTransition(transition);
            transition.setOutgoingVertex(entryPoint_);

        }

        public void addOutgoingTransition(ITransition transition) {
            // Map the transaction endpoint to the Exit pseudostate
            exitPoint_.addOutgoingTransition(transition);
            transition.setIncomingVertex(exitPoint_);
        }

        public List<Set<ITransition>> getAllPossibleOutgoingTransitions() {
            return exitPoint_.getAllPossibleOutgoingTransitions();
        }
    }

    public void addStateEntryListener(boolean recurse, IStateEntryListener listener) {
        if (null == listener) {
            String msg = "null state entry listener specified";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }
        stateBehaviour.addStateEntryListener(recurse, listener);
    }

    public void addIncomingTransition(ITransition transition) {
        stateBehaviour.addIncomingTransition(transition);
    }

    public void addOutgoingTransition(ITransition transition) {
        stateBehaviour.addOutgoingTransition(transition);
    }

    public List<Set<ITransition>> getAllPossibleOutgoingTransitions() {
        return stateBehaviour.getAllPossibleOutgoingTransitions();
    }

    public Set<ITrigger> getDeferrableTriggers() {
        return deferrableTriggers;
    }

    public void setDeferrableTriggers(Set<ITrigger> triggerList) {
        deferrableTriggers = triggerList;
    }

    public boolean defersEvent(org.suggs.fsm.event.IEvent event) {
        for (ITrigger trigger : deferrableTriggers) {
            if (trigger.getEvent().getQualifiedName().equals(event.getType())) {
                return true;
            }
        }
        return false;
    }

}
