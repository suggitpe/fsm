package org.suggs.fsm.engine.impl;

import org.suggs.fsm.engine.uml2.namespacemgt.NamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;

public class NamespaceContext implements INamespaceContext {

    private INamespaceObjectManager namespaceObjectManager = new NamespaceObjectManager();

    public INamespaceObjectManager getNamespaceObjectManager() {
        return namespaceObjectManager;
    }

    public void setNamepaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        this.namespaceObjectManager = namespaceObjectManager;
    }

    /**
     * Returns a String representation of this object using the default toString style.
     */
    @Override
    public String toString() {
        return "NamespaceContext{" +
                "namespaceObjectManager=" + namespaceObjectManager +
                '}';
    }
}
