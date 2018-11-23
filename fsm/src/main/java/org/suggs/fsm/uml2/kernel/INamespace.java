package org.suggs.fsm.uml2.kernel;

import java.util.Set;

public interface INamespace extends INamedElement {

    /**
     * Standard identifier used to mark a top-level namespace.
     */
    String TOP_LEVEL_NAMESPACE = "top";

    /**
     * Standard delimiter for separating namespaces and named
     * elements.
     */
    String NAMESPACE_SEPARATOR = "::";

    /**
     * Gets the named elements owned by this namespace. This typically
     * means the states within a region, for example.
     */
    Set getOwnedMembers();

    /**
     * Sets the named elements owned by this namespace.
     */
    void setOwnedMembers(Set ownedMembers);

    /**
     * Adds a named element to this namespace and sets its namespace
     * attribute. If an object with the same getName() return value is
     * already owned by this namespace, then it will be replaced. The
     * namespace of the replaced named element will be set to
     * <code>null</code>.
     */
    void addOwnedMember(INamedElement newMember);
}
