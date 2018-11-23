package org.suggs.fsm.engine.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.suggs.fsm.engine.uml2.namespacemgt.NamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;

import static org.suggs.fsm.common.StringStyle.DEFAULT_TO_STRING_STYLE;

public class NamespaceContext implements INamespaceContext {

    private INamespaceObjectManager namespaceObjectManager_ = new NamespaceObjectManager();

    public INamespaceObjectManager getNamespaceObjectManager() {
        return namespaceObjectManager_;
    }

    public void setNamepaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        namespaceObjectManager_ = namespaceObjectManager;
    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    public String toString() {
        return new ToStringBuilder(this, DEFAULT_TO_STRING_STYLE).appendSuper(super.toString())
                .append("namespaceObjectManager", namespaceObjectManager_)
                .toString();
    }
}
