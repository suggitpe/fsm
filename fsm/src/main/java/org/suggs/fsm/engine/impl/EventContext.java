package org.suggs.fsm.engine.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

import java.util.HashMap;
import java.util.Map;

import static org.suggs.fsm.common.StringStyle.DEFAULT_TO_STRING_STYLE;

public class EventContext implements IEventContext {
    private IEvent event_;

    private Map context_ = new HashMap();

    private String internalEventType_ = null;

    private IEventFactory eventFactory_;

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    public String toString() {
        return new ToStringBuilder(this, DEFAULT_TO_STRING_STYLE).appendSuper(super.toString())
                .append("event", event_)
                .append("context", context_)
                .toString();
    }

    public Map getContext() {
        return context_;
    }

    public void setEvent(IEvent event) {
        event_ = event;
    }

    /**
     * Returns the value of eventFactory.
     */
    public IEventFactory getEventFactory() {
        return eventFactory_;
    }

    /**
     * Sets the eventFactory field to the specified value.
     */
    public void setEventFactory(IEventFactory eventFactory) {
        eventFactory_ = eventFactory;
    }

    public IEvent getEvent() {
        return event_;
    }

    public void setContext(Map context) {
        context_ = context;
    }

    /**
     * Requests that an internal event be created. The FSM will handle
     * the actual creation and firing of the event. This should mainly be used
     * by actions.
     */
    public void setInternalEvent(String eventType) {
        internalEventType_ = eventType;
    }

    /**
     * Gets the name of the internal event that was requested,
     * or <code>null</code> if no internal event creation has been requested.
     *
     * @return name of internal event or null.
     */
    public String getInternalEvent() {
        return internalEventType_;
    }

    /**
     * Removes any internal event request. This will be called by the FSM
     * after it has processed the internal event request.
     */
    public void removeInternalEvent() {
        internalEventType_ = null;
    }
}
