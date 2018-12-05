package org.suggs.fsm.event;

import org.suggs.fsm.bo.BusinessObjectIdentifier;
import org.suggs.fsm.bo.IBusinessObject;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Base implementation for all events.
 */
public class BaseEvent implements IEvent {

    protected String eventId_;
    protected String type_;
    protected long dispatchTime_;
    protected BusinessObjectIdentifier boId_;
    protected Collection relatedBoIds_ = null;

    private IBusinessObject businessObject_;

    /**
     * Protected constructor for sub-classes to call when constructing
     * from other representations.
     */
    protected BaseEvent() {
        super();
    }

    protected BaseEvent(String type, BusinessObjectIdentifier boId) {
        this(type, boId, new LinkedList());
    }

    protected BaseEvent(String type, BusinessObjectIdentifier boId, Collection relatedBoIds) {
        super();
        eventId_ = null;
        type_ = type;
        boId_ = boId;
        dispatchTime_ = -1;
        relatedBoIds_ = relatedBoIds;
    }

    protected BaseEvent(String eventId, String type, long dispatchTime, BusinessObjectIdentifier boId) {
        this(eventId, type, dispatchTime, boId, new LinkedList());
    }

    protected BaseEvent(String eventId, String type, long dispatchTime, BusinessObjectIdentifier boId, Collection relatedBoIds) {
        super();
        eventId_ = eventId;
        type_ = type;
        boId_ = boId;
        dispatchTime_ = dispatchTime;
        relatedBoIds_ = relatedBoIds;
    }

    public String getEventId() {
        return eventId_;
    }

    public String getType() {
        return type_;
    }

    public void setType(String type) {
        type_ = type;
    }

    public BusinessObjectIdentifier getBusinessObjectIdentifier() {
        return boId_;
    }

    /**
     * Returns the value of relatedBusinessObjectIdentifiers.
     *
     * @return Returns the relatedBusinessObjectIdentifiers.
     */
    public Collection getRelatedBusinessObjectIdentifiers() {
        return relatedBoIds_;
    }

    public long getDispatchTime() {
        return dispatchTime_;
    }

    public IBusinessObject getBusinessObject() {
        return businessObject_;
    }

    public void setBusinessObject(IBusinessObject businessObject) {
        businessObject_ = businessObject;
    }

    /**
     * Returns a string representation of an instance of this object
     * using the StringStyle.DEFAULT_TO_STRING_STYLE.
     */
    @Override
    public String toString() {
        return "BaseEvent{" +
                "eventId_='" + eventId_ + '\'' +
                ", type_='" + type_ + '\'' +
                ", dispatchTime_=" + dispatchTime_ +
                ", boId_=" + boId_ +
                ", relatedBoIds_=" + relatedBoIds_ +
                ", businessObject_=" + businessObject_ +
                '}';
    }

}
