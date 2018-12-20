package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;

public class FinalStateChecker implements IFinalStateChecker {

    public void checkConstraints(IState finalState) {

        /*
         * [1] A final state cannot have any outgoing transitionBuilders.
         */
        Assert.state(finalState.getOutgoing().size() == 0, "Final state " + finalState + " has outgoing transitionBuilders");

        /*
         * [2] A final state cannot have region.
         */
        Assert.state(finalState.getRegion() == null, "Final state " + finalState + " has region");

        /*
         * [3] A final state cannot reference a submachine.
         */

        /*
         * [4] A final state has no entry behavior.
         */
        Assert.state(null == finalState.getEntryBehavior(), "Final state " + finalState + " has entry behavior");

        /*
         * [5] A final state has no exit behavior.
         */
        Assert.state(null == finalState.getExitBehavior(), "Final state " + finalState + " has exit behavior");

        /*
         * [6] A final state has no state (doActivity) behavior.
         */

    }
}
