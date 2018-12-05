package org.suggs.fsm.uml2.scribe.constraints;

/**
 * Objects that implement this interface can be checked for
 * consistency with defined structural constraints.
 */
public interface IConstrainedObject {

    /**
     * Checks the constraints defined for this object (and any owned
     * objects) using the supplied constraint checker.
     *
     * @param constraintVisitor The constraint checker to use.
     */
    void acceptConstraintVisitor(IConstraintVisitor constraintVisitor);

}
