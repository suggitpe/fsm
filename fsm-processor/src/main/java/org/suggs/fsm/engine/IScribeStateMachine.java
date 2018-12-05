package org.suggs.fsm.engine;

import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.framework.api.IStateMachine;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;

/**
 * The internal interface for a state machine, offering additional construction and optimisation methods over
 * the public state machine interface */
public interface IScribeStateMachine extends IStateMachine {

    /**
     * Sets the BehavioredClassifier to be used by the state machine. The behaviored classifier contains the
     * state machine, which is a kind of behavior. */
    void setBehavioredClassifier(IBehavioredClassifier context);

    /**
     * Sets the INamespaceObject manager to be used by the state machine for storing and retrieving references
     * to namespace objects. */
    void setNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager);

    /**
     * Populates the INamespaceObjectManager with name -> object references pairs by inspecting the state machine
     * object graph. */
    void initialiseNamespaceContext();

    /**
     * Checks the internal state machine using the specified IConstraintVisitor instance. */
    void checkStateMachine(IConstraintVisitor constraintChecker);

    /**
     * Optimises the state model for runtime. */
    void optimiseStateModel(IModelOptimiser modelOptimiser);

    /**
     * Sets the IEventContextFactory to use when creating IEventContext objects for events. */
    void setEventContextFactory(IEventContextFactory eventContextFactory);

    IEventContextFactory getEventContextFactory();

    /**
     * Sets the event factory to be used by the state machine to generate internal events. */
    void setEventFactory(IEventFactory eventFactory);

    /**
     * Gets the event factory configured for the state machine. */
    IEventFactory getEventFactory();
}
