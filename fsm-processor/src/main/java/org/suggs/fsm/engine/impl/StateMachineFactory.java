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

    private static final Logger LOG = LoggerFactory.getLogger(StateMachineFactory.class);

    private IBehavioredClassifierBuilder behavioredClassifierBuilder;
    private IEventContextFactory eventContextFactory;
    private IEventFactory eventFactory;
    private IConstraintVisitor constraintChecker;
    private IModelOptimiser modelOptimiser;
    private IFsmEventInterceptor fsmEventInterceptor;


    public IStateMachine createStateMachine(String stateModelId) {
        LOG.debug("Creating state machine for stateModelId=" + stateModelId);

        ScribeStateMachine scribeStateMachine = new ScribeStateMachine();
        IBehavioredClassifier behavioredClassifier =
                behavioredClassifierBuilder.createBehavioredClassifier(stateModelId);
        scribeStateMachine.setBehavioredClassifier(behavioredClassifier);
        scribeStateMachine.setEventMapper(new DefaultEventMapper(behavioredClassifier.getName()));
        scribeStateMachine.setEventContextFactory(eventContextFactory);
        scribeStateMachine.setEventFactory(eventFactory);
        scribeStateMachine.checkStateMachine(constraintChecker);
        scribeStateMachine.initialiseNamespaceContext();
        setEventInterceptor(scribeStateMachine);
        scribeStateMachine.optimiseStateModel(modelOptimiser);
        LOG.debug("Returning new State Machine: " + scribeStateMachine.toString());
        return scribeStateMachine;
    }

    public IBehavioredClassifierBuilder getBehavioredClassifierBuilder() {
        return behavioredClassifierBuilder;
    }

    public void setBehavioredClassifierBuilder(IBehavioredClassifierBuilder stateMachineContextBuilder) {
        behavioredClassifierBuilder = stateMachineContextBuilder;
    }

    /**
     * Gets the event context factory passed to all state machines created by this factory.
     */
    public IEventContextFactory getEventContextFactory() {
        return eventContextFactory;
    }

    /**
     * Sets the event context factory passed to all state machines created by this factory. Must not be <code>null</code>.
     */
    public void setEventContextFactory(IEventContextFactory eventContextFactory) {
        if (null == eventContextFactory) {
            String msg = "null eventContextFactory provided to " + this.toString();
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        this.eventContextFactory = eventContextFactory;
    }

    public IEventFactory getEventFactory() {
        return eventFactory;
    }

    public void setEventFactory(IEventFactory eventFactory) {
        if (null == eventFactory) {
            String msg = "null eventFactory provided to " + this.toString();
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        this.eventFactory = eventFactory;
    }

    public IConstraintVisitor getConstraintChecker() {
        return constraintChecker;
    }

    public void setConstraintChecker(IConstraintVisitor constraintChecker) {
        this.constraintChecker = constraintChecker;
    }

    public void setFsmEventInterceptor(IFsmEventInterceptor eventInterceptor) {
        fsmEventInterceptor = eventInterceptor;
    }

    /**
     * Copies the event interceptor set on this object to another IFsmEventInterceptorManager.
     */
    private void setEventInterceptor(IFsmEventInterceptorManager fsmEventInterceptorManager) {
        if (null != fsmEventInterceptor) {
            fsmEventInterceptorManager.setFsmEventInterceptor(fsmEventInterceptor);
        }
    }

    public IModelOptimiser getModelOptimiser() {
        return modelOptimiser;
    }

    public void setModelOptimiser(IModelOptimiser modelFinaliser) {
        this.modelOptimiser = modelFinaliser;
    }
}
