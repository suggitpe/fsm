package org.suggs.fsm.engine;

import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.framework.api.IStateMachineFactory;
import org.suggs.fsm.framework.spi.IBehavioredClassifierBuilder;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;

public interface IScribeStateMachineFactory extends IStateMachineFactory {

    /**
     * Gets the event context factory passed to all state machines created by this factory.
     */
    IEventContextFactory getEventContextFactory();

    /**
     * Sets the event context factory passed to all state machines created by this factory. Must not be <code>null</code>.
     */
    void setEventContextFactory(IEventContextFactory eventContextFactory);

    /**
     * Gets the event factory passed to all state machines created by this factory.
     */
    IEventFactory getEventFactory();

    /**
     * Sets the event factory passed to all state machines created by this factory. Must not be <code>null</code>.
     */
    void setEventFactory(IEventFactory eventFactory);

    /**
     * Gets the behaviored classifier (state machine model) builder currently configured for this factory.
     */
    IBehavioredClassifierBuilder getBehavioredClassifierBuilder();

    /**
     * Sets the builder object to be used to create state machines.
     */
    void setBehavioredClassifierBuilder(IBehavioredClassifierBuilder stateMachineContextBuilder);

    /**
     * Sets the IConstraintVisitor implementation to be used to check state machines for OCL constraint violations.
     */
    void setConstraintChecker(IConstraintVisitor constraintChecker);

    /**
     * Gets the constraint checker used to check state machine models as part of the model construction process.
     */
    IConstraintVisitor getConstraintChecker();

    /**
     * Gets the model optimiser used to finalise the state machine model after construction but prior to event handling.
     */
    IModelOptimiser getModelOptimiser();

    /**
     * Sets the model optimiser to use. Optimisation may include processes to improve runtime performance.
     */
    void setModelOptimiser(IModelOptimiser modelOptimiser);

}
