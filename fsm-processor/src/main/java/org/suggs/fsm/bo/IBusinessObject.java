package org.suggs.fsm.bo;

/**
 * Top level interface for all business objects within the Scribe
 * system.
 */
public interface IBusinessObject extends Cloneable {

    /**
     * Returns the current composite Id of the business
     * object.
     */
    BusinessObjectIdentifier getBusinessObjectId();

    /**
     * Sets the current composite id of the business object.
     */
    void setBusinessObjectId(BusinessObjectIdentifier businessObjectId);


    /**
     * The busines object's unique identifier that may be used as part
     * of a primary key (compounded with version).
     */
    String getId();

    /**
     * Returns the internal type of the business object.
     *
     * @return
     */
    String getInternalType();

}
