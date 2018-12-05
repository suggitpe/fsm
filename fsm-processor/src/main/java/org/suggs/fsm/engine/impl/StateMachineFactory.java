package org.suggs.fsm.engine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.engine.IEventContextFactory;
import org.suggs.fsm.engine.IFsmEventInterceptorManager;
import org.suggs.fsm.engine.IScribeStateMachineFactory;
import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.framework.api.IStateMachine;
import org.suggs.fsm.framework.spi.IBehavioredClassifierBuilder;
import org.suggs.fsm.framework.spi.IFsmEventInterceptor;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;

public class StateMachineFactory implements IScribeStateMachineFactory {

    private IBehavioredClassifierBuilder behavioredClassifierBuilder_;

    private static final Logger LOG = LoggerFactory.getLogger(StateMachineFactory.class);

    private IEventContextFactory eventContextFactory_;

    /**
     * The facotry to use to create events within the state machine.
     * The main example of events that need to be created within the
     * FSM are completion events to indicate that a state has been
     * entered successfully.
     */
    private IEventFactory eventFactory_;

    private IConstraintVisitor constraintChecker_;

    private IModelOptimiser modelOptimiser_;

    /**
     * The fsmEventInterceptor that should be applied to all state
     * machines created by this factory
     */
    private IFsmEventInterceptor fsmEventInterceptor_ = null;

    public IStateMachine getStateMachine(String stateModelId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating state machine for stateModelId=" + stateModelId);
        }

        ScribeStateMachine scribeStateMachine = new ScribeStateMachine();

        IBehavioredClassifier behavioredClassifier =
                behavioredClassifierBuilder_.createBehavioredClassifier(stateModelId);

        scribeStateMachine.setBehavioredClassifier(behavioredClassifier);

        scribeStateMachine.setEventMapper(new DefaultEventMapper(behavioredClassifier.getName()));

        scribeStateMachine.setEventContextFactory(eventContextFactory_);

        scribeStateMachine.setEventFactory(eventFactory_);

        scribeStateMachine.checkStateMachine(constraintChecker_);

        scribeStateMachine.initialiseNamespaceContext();

        setEventInterceptor(scribeStateMachine);

        scribeStateMachine.optimiseStateModel(modelOptimiser_);

        LOG.debug("Returning new State Machine: " + scribeStateMachine.toString());

        return scribeStateMachine;
    }

    public IBehavioredClassifierBuilder getBehavioredClassifierBuilder() {
        return behavioredClassifierBuilder_;
    }

    public void setBehavioredClassifierBuilder(IBehavioredClassifierBuilder stateMachineContextBuilder) {
        behavioredClassifierBuilder_ = stateMachineContextBuilder;
    }

    /**
     * Gets the event context factory passed to all state machines
     * created by this factory.
     */
    public IEventContextFactory getEventContextFactory() {
        return eventContextFactory_;
    }

    /**
     * Sets the event context factory passed to all state machines
     * created by this factory. Must not be <code>null</code>.
     */
    public void setEventContextFactory(IEventContextFactory eventContextFactory) {

        if (null == eventContextFactory) {
            String msg = "null eventContextFactory provided to " + this.toString();
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        this.eventContextFactory_ = eventContextFactory;
    }

    /**
     * Gets the event factory passed to all state machines created by
     * this factory.
     */
    public IEventFactory getEventFactory() {
        return eventFactory_;
    }

    /**
     * Sets the event factory passed to all state machines created by
     * this factory. Must not be <code>null</code>.
     */
    public void setEventFactory(IEventFactory eventFactory) {

        if (null == eventFactory) {
            String msg = "null eventFactory provided to " + this.toString();
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        eventFactory_ = eventFactory;
    }

    public IConstraintVisitor getConstraintChecker() {
        return constraintChecker_;
    }

    public void setConstraintChecker(IConstraintVisitor constraintChecker) {
        constraintChecker_ = constraintChecker;
    }

    public void setFsmEventInterceptor(IFsmEventInterceptor eventInterceptor) {

        fsmEventInterceptor_ = eventInterceptor;

    }

    /**
     * Copies the event interceptor set on this object to another
     * IFsmEventInterceptorManager.
     */
    private void setEventInterceptor(IFsmEventInterceptorManager fsmEventInterceptorManager) {

        if (null != fsmEventInterceptor_) {
            fsmEventInterceptorManager.setFsmEventInterceptor(fsmEventInterceptor_);
        }
    }

    public IModelOptimiser getModelOptimiser() {
        return modelOptimiser_;
    }

    public void setModelOptimiser(IModelOptimiser modelFinaliser) {
        this.modelOptimiser_ = modelFinaliser;
    }
}
