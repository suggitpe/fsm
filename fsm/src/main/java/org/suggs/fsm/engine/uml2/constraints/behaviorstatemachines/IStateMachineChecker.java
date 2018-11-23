package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine;

/**
 * Objects that implement this interface check state machines for
 * constraint violations.
 */
public interface IStateMachineChecker {

    /**
     * Checks a state machine.
     */
    void checkConstraints(IStateMachine stateMachine);
}
