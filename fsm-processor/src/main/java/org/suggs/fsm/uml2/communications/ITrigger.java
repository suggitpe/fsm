package org.suggs.fsm.uml2.communications;

import org.suggs.fsm.uml2.kernel.INamedElement;

/**
 * Defines an implementation of the UML 2.0 Trigger class. Triggers
 * specify events that may cause a transaction to be enabled and
 * fired.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface ITrigger extends INamedElement {

    /**
     * Gets the event that causes the trigger.
     */
    IEvent getEvent();

    /**
     * Sets the event that will cause the trigger.
     */
    void setEvent(IEvent event);

}
