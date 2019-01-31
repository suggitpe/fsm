package org.suggs.fsm.engine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.common.Assert;
import org.suggs.fsm.engine.IEventContextFactory;
import org.suggs.fsm.engine.IFsmEventInterceptorManager;
import org.suggs.fsm.engine.IScribeStateMachine;
import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.event.IEventMapper;
import org.suggs.fsm.framework.spi.IFsmEventInterceptor;
import org.suggs.fsm.framework.spi.IStateManager;
import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;
import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine;
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.kernel.IConstraint;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

import java.util.HashMap;
import java.util.Map;

public class ScribeStateMachine implements IScribeStateMachine {

    private static final Logger LOG = LoggerFactory.getLogger(ScribeStateMachine.class);

    /**
     * Maintains the event interceptors registered with this state machine.
     */
    private FsmEventInterceptorManager fsmEventInterceptorManager;

    /**
     * Includes the namespace object manager for providing object lookup based on qualified name.
     */
    private INamespaceContext namespaceContext;

    /**
     * The context that contains the state machine to pass events to.
     */
    private IBehavioredClassifier behavioredClassifier;

    /**
     * The factory to use to create the event context from an incoming event.
     */
    private IEventContextFactory eventContextFactory;

    /**
     * Contains contextual information for this state machine that is passed to all guards and actions.
     */
    private IStateMachineContext stateMachineContext;

    /**
     * Used to map the name of the event passed to the State Manager to another name used internally. If an
     * event mapper is not defined, then the name of the event is used directly.
     */
    private IEventMapper eventMapper;

    public ScribeStateMachine() {
        super();

        fsmEventInterceptorManager = new FsmEventInterceptorManager();
        namespaceContext = new NamespaceContext();
        stateMachineContext = new StateMachineContext();
        stateMachineContext.setFsmEventInterceptor(fsmEventInterceptorManager);
    }

    public void setBehavioredClassifier(IBehavioredClassifier context) {
        behavioredClassifier = context;
    }

    public IEventMapper getEventMapper() {
        return eventMapper;
    }

    public void setEventMapper(IEventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    /**
     * Initialises the state manager context. First calls <code>IStateManager.getHistoryStates</code> and
     * if <code>null</code> is returned, then an new empty Map is set as the history states via a call
     * to <code>IStateManager.setHistoryStates</code>.
     */
    public void initialiseStateManagerContext(IStateManager stateManager) {

        if (null == stateManager) {
            String msg = "Attempting to set null stateManager";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        // Initialise history states
        if (null == stateManager.getHistoryStates()) {
            LOG.debug("Got null history states from stateManager: " + stateManager);
            // Set history states to a new empty map.
            stateManager.storeHistoryStates(new HashMap<>());
        }

        stateMachineContext.setStateManager(stateManager);
    }

    public void handleEvent(IEvent event, IStateManager stateManager, Map<String, String> context) {

        initialiseStateManagerContext(stateManager);

        //map event
        if (eventMapper != null) {
            event = eventMapper.map(event);
        }

        // Notify listeners
        stateMachineContext.getEventInterceptor().onEventReceived(event, context);
        // Load the state machine state from the state manager
        String activeState = stateMachineContext.getStateManager().getActiveState();

        if (null == activeState) {
            IPseudoState initialState = ((IStateMachine) behavioredClassifier.getOwnedBehavior()).getOwnedRegion().getInitialState();

            String msg = "No current state provided by " + stateMachineContext.getStateManager()
                    + ". Initialising to pseudoStateKind state for top region, " + initialState;
            // Enter the pseudoStateKind state for the state machine
            initialState.enter(eventContextFactory.createEventContext(event, context, getEventFactory()),
                    namespaceContext,
                    stateMachineContext);

            activeState = stateMachineContext.getStateManager().getActiveState();
        } else if (IState.TRANSITIONING.equals(activeState)) {
            /*
             * A previous transition on the state machine failed and
             * the state machine is in an indeterminate state. This is
             * bad.
             */
            String msg = "The current active state could not be determined. " + stateMachineContext.getStateManager() + " returned "
                    + activeState;
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        // Request the current state to handle the event
        IState currentState = null;
        try {
            currentState = (IState) namespaceContext.getNamespaceObjectManager().getObject(activeState);
        } catch (ClassCastException e) {
            String msg = activeState + " returned from " + stateMachineContext.getStateManager() + " is not a stable state.";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        currentState.processEvent(eventContextFactory.createEventContext(event, context, getEventFactory()),
                namespaceContext,
                stateMachineContext);

        // Check that the current active state is not null and is
        // valid
        activeState = stateMachineContext.getStateManager().getActiveState();
        Assert.state(null != activeState, "State machine " + this + " left the current active state null.");
        Assert.state(null != namespaceContext.getNamespaceObjectManager().getObject(activeState),
                "State machine " + this + " stored non-namespace object as current state: " + activeState);

        Assert.state(!IState.TRANSITIONING.equals(activeState), "State machine " + this + " is in an indeterminate state: "
                + IState.TRANSITIONING);

        // we should never end up in a transient state
        if (null != activeState) {
            IState endState = (IState) namespaceContext.getNamespaceObjectManager().getObject(activeState);

            if (null != endState) {
                if (endState.isTransient()) {
                    String msg = "It is invalid for a state machine to end up in a transient state.";
                    LOG.error(msg);
                    throw new IllegalStateException(msg);
                }
            }
        }
    }

    public INamespaceObjectManager getNamespaceObjectManager() {
        return namespaceContext.getNamespaceObjectManager();
    }

    public void setNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        namespaceContext.setNamepaceObjectManager(namespaceObjectManager);
    }

    public void initialiseNamespaceContext() {
        LOG.debug("Initialising namespace context");
        behavioredClassifier.acceptNamespaceObjectManager(namespaceContext.getNamespaceObjectManager());
    }

    public void setEventContextFactory(IEventContextFactory eventContextFactory) {
        this.eventContextFactory = eventContextFactory;
    }

    public IEventFactory getEventFactory() {
        return stateMachineContext.getEventFactory();
    }

    public void setEventFactory(IEventFactory eventFactory) {
        stateMachineContext.setEventFactory(eventFactory);
    }

    public IEventContextFactory getEventContextFactory() {
        return eventContextFactory;
    }

    public void setFsmEventInterceptor(IFsmEventInterceptor eventInterceptor) {
        fsmEventInterceptorManager.setFsmEventInterceptor(eventInterceptor);
    }

    public void checkStateMachine(IConstraintVisitor constraintVisitor) {
        LOG.debug("Checking constraints");
        behavioredClassifier.acceptConstraintVisitor(constraintVisitor);
    }

    public void optimiseStateModel(IModelOptimiser modelOptimiser) {
        LOG.debug("Optimising state model");
        behavioredClassifier.acceptOptimiser(modelOptimiser);
    }

    /**
     * Returns a String representation of this object using the default toString style.
     */
    @Override
    public String toString() {
        return "ScribeStateMachine{" +
                "fsmEventInterceptorManager=" + fsmEventInterceptorManager +
                ", namespaceContext=" + namespaceContext +
                ", behavioredClassifier=" + behavioredClassifier +
                ", eventContextFactory=" + eventContextFactory +
                ", stateMachineContext=" + stateMachineContext +
                ", eventMapper=" + eventMapper +
                '}';
    }

    /**
     * Manages and notifies event listeners.
     */
    class FsmEventInterceptorManager implements IFsmEventInterceptorManager, IFsmEventInterceptor {

        private IFsmEventInterceptor fsmEventInterceptor_ = null;

        public void setFsmEventInterceptor(IFsmEventInterceptor eventInterceptor) {
            fsmEventInterceptor_ = eventInterceptor;
        }

        public void onActionExecuted(IBehavior action, IEventContext eventContext, INamespaceContext namespaceContext,
                                     IStateMachineContext stateMachineContext) {
            if (null != fsmEventInterceptor_) {
                fsmEventInterceptor_.onActionExecuted(action, eventContext, namespaceContext, stateMachineContext);
            }
        }

        public void onEventReceived(IEvent event, Map<String, String> context) {

            if (null != fsmEventInterceptor_) {
                fsmEventInterceptor_.onEventReceived(event, context);
            }
        }

        public void onEventSkipped(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {

            if (null != fsmEventInterceptor_) {
                fsmEventInterceptor_.onEventSkipped(eventContext, namespaceContext, stateMachineContext);
            }

        }

        public void onGuardEvaluated(IConstraint guard, boolean result, IEventContext eventContext, INamespaceContext namespaceContext,
                                     IStateMachineContext stateMachineContext) {

            if (null != fsmEventInterceptor_) {
                fsmEventInterceptor_.onGuardEvaluated(guard, result, eventContext, namespaceContext, stateMachineContext);
            }

        }

        public void onTransitionComplete(ITransition transition, IState sourceState, IState targetState, IEventContext eventContext,
                                         INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {

            if (null != fsmEventInterceptor_) {
                fsmEventInterceptor_.onTransitionComplete(transition,
                        sourceState,
                        targetState,
                        eventContext,
                        namespaceContext,
                        stateMachineContext);
            }

        }

        public void onDeferredEventDiscarded(IEventContext eventContext) {
            if (null != fsmEventInterceptor_) {
                fsmEventInterceptor_.onDeferredEventDiscarded(eventContext);
            }
        }

        public void onEventDeferred(IEventContext eventContext) {
            if (null != fsmEventInterceptor_) {
                fsmEventInterceptor_.onEventDeferred(eventContext);
            }
        }

        @Override
        public String toString() {
            return "FsmEventInterceptorManager{" +
                    "fsmEventInterceptor_=" + fsmEventInterceptor_ +
                    '}';
        }
    }

}
