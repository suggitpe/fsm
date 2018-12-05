package org.suggs.fsm.engine.uml2.constraints.kernel;

import org.suggs.fsm.uml2.kernel.INamedElement;

/**
 * Objects that implement this interface check named elements for
 * constraint violations.
 */
public interface INamedElementChecker
{

    /**
     * Checks a named element.
     */
    void checkConstraints( INamedElement namedElement );

}
