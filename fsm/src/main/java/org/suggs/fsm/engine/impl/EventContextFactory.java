package org.suggs.fsm.engine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.engine.IEventContextFactory;
import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

import java.util.Map;

public class EventContextFactory implements IEventContextFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EventContextFactory.class);

    public IEventContext createEventContext(IEvent event, Map<String, String> context, IEventFactory factory) {

        EventContext context_ = new EventContext();

        if (null == event) {
            String msg = "null event specified in call to createEventContext.";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        context_.setEvent(event);

        context_.setContext(context);

        context_.setEventFactory(factory);

        return context_;
    }
}
