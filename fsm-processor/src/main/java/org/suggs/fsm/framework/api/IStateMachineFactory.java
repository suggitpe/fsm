package org.suggs.fsm.framework.api;

import org.suggs.fsm.engine.IFsmEventInterceptorManager;

/**
 * Implementations of this interface create state machines based on a specified state model. The state
 * machine to create is uniquely identified by a String ID.
 */
public interface IStateMachineFactory extends IFsmEventInterceptorManager {

    /**
     * Constructs and returns a state machine based on the specified state model identifier. <br>
     * The state machine will be <i>construcuted </i> but not <i>bound </i> meaning that it is in a generic
     * state, possibly with guard and action implemenations set where the implementations can be set without
     * knowing the business object context of the state machine.
     */
    IStateMachine createStateMachine(String stateModelId);

}
