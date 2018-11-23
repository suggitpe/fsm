package org.suggs.fsm.engine.uml2.constraints.communications;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.engine.uml2.constraints.kernel.NamedElementChecker;
import org.suggs.fsm.uml2.communications.ITrigger;
import org.suggs.fsm.uml2.kernel.INamedElement;

public class TriggerChecker extends NamedElementChecker implements ITriggerChecker {

    public void checkConstraints(ITrigger trigger) {
        // Triggers must have an event
        Assert.state(null != trigger.getEvent(), "Trigger can not have a null event");

        // Check named element aspects
        checkConstraints((INamedElement) trigger);
    }
}
