package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine;

public class StateMachineChecker implements IStateMachineChecker {

    public void checkConstraints(IStateMachine stateMachine) {

        /*
         * [1] The classifier context of a state machine cannot be an
         * interface.
         */

        /*
         * [2] The context classifier of the method state machine of a
         * behavioral feature must be the classifier that owns the
         * behavioral feature
         */

        /*
         * [3] The connection points of a state machine are
         * pseudostates of kind entry point or exit point.
         */

        /*
         * [4] A state machine as the method for a behavioral feature
         * cannot have entry/exit connection points.
         */

    }
}
