package org.suggs.fsm.engine.uml2.constraints.kernel;

import org.suggs.fsm.uml2.kernel.IConstraint;

/**
 * Objects that implement this interface check (UML metamodel)
 * constraints for constraint violations.
 */
public interface IConstraintChecker {

    /**
     * Checks a constraint.
     */
    void checkConstraint(IConstraint constraint);

}
