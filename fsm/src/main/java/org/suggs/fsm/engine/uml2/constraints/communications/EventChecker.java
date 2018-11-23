package org.suggs.fsm.engine.uml2.constraints.communications;

import org.suggs.fsm.engine.uml2.constraints.kernel.NamedElementChecker;
import org.suggs.fsm.uml2.communications.IEvent;

public class EventChecker extends NamedElementChecker implements IEventChecker {

    public void checkConstraints(IEvent event) {

        // There are no event-specific constraints

        // Check NamedElement aspects
        super.checkConstraints(event);

    }
}
