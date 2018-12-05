package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.uml2.behaviorstatemachines.IVertex;

/**
 * Objects that implement this interface check vertices for constraint
 * violations.
 */
public interface IVertexChecker {

    /**
     * Checks a vertex.
     */
    void checkConstraints(IVertex vertex);

}
