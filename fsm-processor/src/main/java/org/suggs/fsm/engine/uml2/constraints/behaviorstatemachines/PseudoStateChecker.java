package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState;

public class PseudoStateChecker implements IPseudoStateChecker {

    public void checkConstraints(IPseudoState pseudostate) {
        /*
         * [1] An initial vertex can have at most one outgoing
         * transition.
         */
        if (IPseudoState.INITIAL.equals(pseudostate.getPseudoStateKind())) {
            Assert.state(pseudostate.getOutgoing().size() <= 1, "Initial state " + pseudostate + "  has "
                    + pseudostate.getOutgoing().size() + " outgoing transitions");
        }

        /*
         * [2] History vertices can have at most one outgoing
         * transition.
         */
        if (IPseudoState.DEEP_HISTORY.equals(pseudostate.getPseudoStateKind())
                || IPseudoState.SHALLOW_HISTORY.equals(pseudostate.getPseudoStateKind())) {
            Assert.state(pseudostate.getOutgoing().size() <= 1, "History state " + pseudostate + " has "
                    + pseudostate.getOutgoing().size() + " outgoing transitions");
        }

        /*
         * Constraints relating to forks, joins and choices have been
         * omitted
         */

        /*
         * [9] Pseudostates of kind entryPoint can only be defined in
         * the topmost regions of a StateMachine.
         */

        /*
         * [9] Pseudostates of kind exitPoint can only be defined in
         * the topmost regions of a StateMachine.
         */

    }
}
