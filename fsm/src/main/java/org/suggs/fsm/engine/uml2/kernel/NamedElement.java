/*
 * NamedElement.java created on 04-Nov-2005 11:45:01 by billinro for project FsmLib
 *
 * Copyright (c) 2005 UBS Investment Bank
 */
package org.suggs.fsm.engine.uml2.kernel;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.kernel.INamespace;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;

import static org.suggs.fsm.common.StringStyle.DEFAULT_TO_STRING_STYLE;

public abstract class NamedElement implements INamedElement {

    private String name_ = EMPTY_NAME;

    private INamespace namespace_ = null;

    /**
     * Used to cache the qualified name
     */
    private String qualifiedName_ = null;

    public String getName() {
        return name_;
    }

    public INamespace getNamespace() {
        return namespace_;
    }

    public String getQualifiedName() {

        if (null == qualifiedName_) {

            /*
             * Generate the qualified name in accordance with the UML
             * specification guidelines: 1) If there is no name, or
             * one of the containing namespaces has no name, there is
             * no qualified name. 2) When there is a name, and all of
             * the containing namespaces have a name, the qualified
             * name is constructed from the names of the containing
             * namespaces.
             */
            if (null == namespace_) {
                /*
                 * This element does not belong to a namespace. If
                 * this element has an empty name, then the qualified
                 * name is an empty name. If this element has a name,
                 * then we create a qualified name baed on a top-level
                 * namespace name.
                 */
                qualifiedName_ = (INamespace.EMPTY_NAME.equals(name_) ? INamespace.EMPTY_NAME : INamespace.TOP_LEVEL_NAMESPACE
                        + INamespace.NAMESPACE_SEPARATOR + name_);
            } else if (INamespace.EMPTY_NAME.equals(namespace_.getQualifiedName())) {
                // This element has a namespace, but its qualified
                // name is empty. The qualified name of this element
                // is therefore empty
                qualifiedName_ = INamespace.EMPTY_NAME;
            } else {
                // The normal case, the qualified name is that of the
                // namespace with this element's name appended
                qualifiedName_ = namespace_.getQualifiedName() + INamespace.NAMESPACE_SEPARATOR + name_;
            }

        }

        return qualifiedName_;
    }

    public void setName(String name) {
        name_ = (null == name ? EMPTY_NAME : name);
    }

    public void setNamespace(INamespace namespace) {
        namespace_ = namespace;
    }

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        constraintVisitor.visitNamedElement(this);
    }

    public void acceptNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        namespaceObjectManager.visitNamedElement(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        boolean ret = false;
        if (obj instanceof NamedElement) {
            NamedElement rhs = (NamedElement) obj;
            EqualsBuilder eq = new EqualsBuilder().append(name_, rhs.name_);
            if (this != namespace_) {
                eq.append(namespace_, rhs.namespace_);
            }
            ret = eq.isEquals();
        }
        return ret;
    }

    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder(5, 79).append(name_);
        if (this != namespace_) {
            hcb.append(namespace_);
        }
        return hcb.toHashCode();
    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    public String toString() {
        return new ToStringBuilder(this, DEFAULT_TO_STRING_STYLE).appendSuper(super.toString())
                .append("name", name_)
                .append("qualifiedName", getQualifiedName())
                .toString();
    }

}
