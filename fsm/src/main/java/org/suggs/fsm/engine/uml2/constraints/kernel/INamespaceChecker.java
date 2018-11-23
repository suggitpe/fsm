package org.suggs.fsm.engine.uml2.constraints.kernel;

import org.suggs.fsm.uml2.kernel.INamespace;

/**
 * Objects that implement this interface check namespaces for
 * constraint violations.
 */
public interface INamespaceChecker {

    /**
     * Checks a namespace.
     */
    void checkConstraints(INamespace namespace);

}
