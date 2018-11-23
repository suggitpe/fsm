package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.uml2.behaviorstatemachines.IVertex;

public class VertexChecker implements IVertexChecker {

    public void checkConstraints(IVertex vertex) {
        /*
         * A vertex must have a container. The UML spec does not
         * identify this constraint, but it should be true for the
         * vertex types used in Scribe state machines, i.e. states and
         * pseudostates.
         */
        Assert.state(null != vertex.getContainer(), "Vertex " + vertex + " has a null container");
    }
}
