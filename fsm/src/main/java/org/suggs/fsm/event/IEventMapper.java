package org.suggs.fsm.event;

public interface IEventMapper {

    /**
     * Maps the specified event to a new event (the return value may
     * be the same object, or null if the mapping could not complete).
     */
    IEvent map(IEvent event);
}
