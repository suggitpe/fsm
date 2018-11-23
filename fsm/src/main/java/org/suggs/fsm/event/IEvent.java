package org.suggs.fsm.event;

import org.suggs.fsm.bo.BusinessObjectIdentifier;
import org.suggs.fsm.bo.IBusinessObject;

import java.util.Collection;

/**
 * Top level interface for all events that are passed between
 * components.
 */
public interface IEvent {

    String PROPERTY_EVENT_ID = "eid";
    String PROPERTY_EVENT_TYPE = "et";
    String PROPERTY_EVENT_DISPATCH_TIME = "edt";
    String PROPERTY_BO_ID = "boid";
    String PROPERTY_BO_OWNER = "boowner";
    String PROPERTY_BO_VERSION = "bover";
    String PROPERTY_BO_INTERNAL_TYPE = "bointtype";
    String PROPERTY_BO_TYPE = "botype";
    String PROPERTY_BUCKET_ID = "bkt";

    /**
     * Returns the event id that has been assigned to this event, or
     * null if none has been assigned (i.e. the event has not been
     * sent). If the event was delivered via JMS then this could be
     * the JMSMessageId.
     *
     * @return The event id that has been assigned to this event, or
     * null if none has been assigned (i.e. the event has not
     * been sent)
     */
    String getEventId();

    /**
     * Returns the type of event this instance represents - an
     * arbitrary value with meaning attached on either side of the
     * messaging bus, but none in between - may never be null.
     *
     * @return The type of event this instance represents
     */
    String getType();

    /**
     * Sets the type of event this instance represents - an arbitrary
     * value with meaning attached on either side of the messaging
     * bus, but none in between - must never be null.
     *
     * @param type
     */
    void setType(String type);

    /**
     * Returns the dispatch time of the event if available - if not
     * available the value will be -ve.
     *
     * @return The dispatch time of the event if available - if not
     * available the value will be -ve.
     */
    long getDispatchTime();

    /**
     * Return identifying information about the business object that
     * this event relates to.
     *
     * @return Identifying information about the business object that
     * this event relates to.
     */
    BusinessObjectIdentifier getBusinessObjectIdentifier();

    /**
     * Returns a collection of BusinessObjectIdentifier objects that
     * may be related to the event's top level
     * BusinessObjectIdentifier.
     *
     * @return A collection of BusinessObjectIdentifier objects that
     * may be related to the event's top level
     * BusinessObjectIdentifier.
     */
    Collection getRelatedBusinessObjectIdentifiers();

    /**
     * Returns the related business object (that must be the object
     * represented by the BusinessObjectIdentifier and
     * BusinessObjectVersion of this class) - may be null if the event
     * has not been enriched.
     *
     * @return The related stateful business object (that must be the
     * object represented by the BusinessObjectIdentifier and
     * BusinessObjectVersion of this class) - may be null if
     * the event has not been enriched.
     */
    IBusinessObject getBusinessObject();

    /**
     * Sets the related business object (that must be the object
     * represented by the BusinessObjectIdentifier and
     * BusinessObjectVersion of this class).
     *
     * @param statefulBusinesObject
     */
    void setBusinessObject(IBusinessObject statefulBusinesObject);

}
