package org.suggs.fsm.uml2.scribe.runtime;

import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.event.IEventFactory;

import java.util.Map;

/**
 * An object that implements this interface provides access to event
 * and contextual information to be provided to the state machine when
 * handling the event.
 */
public interface IEventContext
{
    /**
     * Gets the event.
     */
    IEvent getEvent();

    /**
     * Sets the event.
     */
    void setEvent( IEvent event );

    /**
     * Gets the event factory.
     */
    IEventFactory getEventFactory();
    
    /**
     * Sets the event factory
     */
    void setEventFactory(IEventFactory factory);
      
    
    /**
     * Sets the event context. Event context may include any
     * information that should be used when evaluating guards or
     * executing actions. The FSM does not use or mutate the event
     * context and makes no assumptions as to its content.
     */
    void setContext( Map context );

    /**
     * Gets the context associated with the event.
     */
    Map getContext();
    
    /**
     * Requests that an internal event be created. The FSM will handle
     * the actual creation and firing of the event. This should mainly be used
     * by actions. 
     */
    void setInternalEvent( String eventType );
    
    /**
     * Gets the name of the internal event that was requested, 
     * or <code>null</code> if no internal event creation has been requested. 
     * @return name of internal event or null.
     */
    String getInternalEvent();
    
    /**
     * Removes any internal event request. This will be called by the FSM
     * after it has processed the internal event request.
     */
    void removeInternalEvent();

}
