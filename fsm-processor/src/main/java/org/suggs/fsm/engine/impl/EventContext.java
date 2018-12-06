package org.suggs.fsm.engine.impl;

import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

import java.util.HashMap;
import java.util.Map;

public class EventContext implements IEventContext {
    
    private IEvent event;
    private Map context = new HashMap();
    private String internalEventType = null;
    private IEventFactory eventFactory;

    /**
     * Returns a String representation of this object using the default toString style.
     */
    @Override
    public String toString() {
        return "EventContext{" +
                "event=" + event +
                ", context=" + context +
                ", internalEventType='" + internalEventType + '\'' +
                ", eventFactory=" + eventFactory +
                '}';
    }

    public Map getContext() {
        return context;
    }

    public void setEvent(IEvent event) {
        this.event = event;
    }

    /**
     * Returns the value of eventFactory.
     */
    public IEventFactory getEventFactory() {
        return eventFactory;
    }

    /**
     * Sets the eventFactory field to the specified value.
     */
    public void setEventFactory(IEventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    public IEvent getEvent() {
        return event;
    }

    public void setContext(Map context) {
        this.context = context;
    }

    /**
     * Requests that an internal event be created. The FSM will handle the actual creation and firing of the
     * event. This should mainly be used by actions.
     */
    public void setInternalEvent(String eventType) {
        internalEventType = eventType;
    }

    /**
     * Gets the name of the internal event that was requested, or <code>null</code> if no internal event creation has been requested.
     */
    public String getInternalEvent() {
        return internalEventType;
    }

    /**
     * Removes any internal event request. This will be called by the FSM after it has processed the internal event request.
     */
    public void removeInternalEvent() {
        internalEventType = null;
    }
}
