package org.suggs.fsm.event;

import org.suggs.fsm.bo.IBusinessObject;

/**
 * General event factory - needs to be though out a bit more but this
 * suffices for now.
 */
public interface IEventFactory {

    /**
     * Creates an event bound for a particular business object.
     */
    IEvent createEvent(String type, IEvent event, IBusinessObject businessObject);

    /**
     * Create a new event based on that specified but with the
     * specified type.
     */
    IEvent createEvent(String type, IEvent event);

}
