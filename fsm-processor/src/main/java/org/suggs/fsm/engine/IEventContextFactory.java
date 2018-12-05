package org.suggs.fsm.engine;

import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

import java.util.Map;

/**
 * Implemenations of this interface create IEventContext instances based on an IEvent instance and Map
 * of context information. */
public interface IEventContextFactory
{

    /**
     * Creates an IEventContext. */
    IEventContext createEventContext(IEvent event, Map<String, String> context, IEventFactory factory );

}
