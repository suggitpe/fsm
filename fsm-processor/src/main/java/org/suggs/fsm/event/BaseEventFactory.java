package org.suggs.fsm.event;


import org.suggs.fsm.bo.BusinessObjectIdentifier;
import org.suggs.fsm.bo.IBusinessObject;

/**
 * Default event factory to create DefaultEvent objects.
 */
public class BaseEventFactory implements IEventFactory {

    public IEvent createEvent(String type, IEvent event, IBusinessObject businessObject) {
        return new BaseEvent(type, new BusinessObjectIdentifier(event.getBusinessObjectIdentifier()));
    }

    public IEvent createEvent(String type, IEvent event) {
        return new BaseEvent(type, event.getBusinessObjectIdentifier());
    }

}
