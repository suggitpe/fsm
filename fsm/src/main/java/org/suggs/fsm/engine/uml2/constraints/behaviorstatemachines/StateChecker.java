package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;

public class StateChecker implements IStateChecker {

    public void checkConstraints(IState state) {

        if (IState.SIMPLE.equals(state.getStateKind())) {
            Assert.state(state.getRegion() == null, "Simple states must not have a region");
        } else if (IState.SIMPLE_COMPOSITE.equals(state.getStateKind())) {
            Assert.state(state.getRegion() != null, "Compound states must have a region");
        }
    }
}
