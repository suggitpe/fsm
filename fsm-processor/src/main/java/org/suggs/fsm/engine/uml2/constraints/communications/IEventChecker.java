package org.suggs.fsm.engine.uml2.constraints.communications;

import org.suggs.fsm.uml2.communications.IEvent;

/**
 * Objects that implement this interface check events for constraint
 * violations.
 */
public interface IEventChecker {

    /**
     * Checks an event.
     */
    void checkConstraints(IEvent event);
}
