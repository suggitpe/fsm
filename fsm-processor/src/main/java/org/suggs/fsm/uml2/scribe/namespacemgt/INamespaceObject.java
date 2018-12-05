package org.suggs.fsm.uml2.scribe.namespacemgt;

/**
 * Implementations of this interface accept an INamespaceObjectManager
 * visitor for the purposes of building the object namespace by
 * traversing the state machine object graph.
 */
public interface INamespaceObject
{

    /**
     * Accepts an INamespaceObjectManager visitor, allowing it to
     * interrogate this object and objects owned by it and identify
     * any guards or actions that need to be registered in the
     * namespace of the state machine.
     * 
     * @param namespaceObjectManager
     *            The INamespaceObjectManager visitor.
     */
    void acceptNamespaceObjectManager( INamespaceObjectManager namespaceObjectManager );
}
