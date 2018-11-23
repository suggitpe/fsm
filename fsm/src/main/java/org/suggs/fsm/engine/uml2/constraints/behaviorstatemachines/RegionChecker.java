package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.engine.uml2.constraints.kernel.NamespaceChecker;
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;

public class RegionChecker extends NamespaceChecker implements IRegionChecker {
    public void checkConstraints(IRegion region) {

        // Regions must be owned by a State or a State Machine
        Assert.state(region.getState() != null || region.getStateMachine() != null, "The region is not owned by a state or state machine");

    }
}
