package org.suggs.fsm.engine.uml2.kernel;

import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.kernel.INamespace;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;

import java.util.Objects;

public abstract class NamedElement implements INamedElement {

    private String name_ = EMPTY_NAME;
    private INamespace namespace_ = null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedElement that = (NamedElement) o;
        return Objects.equals(name_, that.name_) &&
                Objects.equals(namespace_, that.namespace_) &&
                Objects.equals(qualifiedName_, that.qualifiedName_);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name_, namespace_, qualifiedName_);
    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    @Override
    public String toString() {
        return "NamedElement{" +
                "name_='" + name_ + '\'' +
                ", namespace_=" + namespace_ +
                ", qualifiedName_='" + qualifiedName_ + '\'' +
                '}';
    }

}
