package org.suggs.fsm.engine.uml2.constraints.communications;

import org.suggs.fsm.uml2.communications.ITrigger;

/**
 * Objects that implement this interface check triggers for constraint
 * violations.
 */
public interface ITriggerChecker {

    /**
     * Checks a trigger.
     */
    void checkConstraints(ITrigger trigger);

}
