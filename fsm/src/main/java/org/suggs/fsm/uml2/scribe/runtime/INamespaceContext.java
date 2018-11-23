package org.suggs.fsm.uml2.scribe.runtime;

import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;

/**
 * Implementations of this interface provide access to context objects
 * that would be included in the UML state machine model.
 * <p>
 * Access to objects mapped to guard and action names is provided via
 * an instance of INamespaceObjectManager.
 */
public interface INamespaceContext {

    /**
     * Gets the namespace object manager from this context.
     *
     * @return The namespace object manager.
     */
    INamespaceObjectManager getNamespaceObjectManager();

    /**
     * Sets the namespace obejct manager to use. The namespace object
     * manager provides naming services for the elements of the state
     * model and is typically used to lookup actions and guards by
     * name.
     *
     * @param namespaceObjectManager The namespace object manager to use.
     */
    void setNamepaceObjectManager(INamespaceObjectManager namespaceObjectManager);

}
