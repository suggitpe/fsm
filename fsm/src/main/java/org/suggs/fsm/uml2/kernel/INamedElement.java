package org.suggs.fsm.uml2.kernel;

import org.suggs.fsm.uml2.scribe.constraints.IConstrainedObject;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObject;

/**
 * Defines an implementation of the UML 2.0 NamedElement class.
 * NamedElements is an element in a mode that may have a name.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface INamedElement extends IConstrainedObject, INamespaceObject {

    /**
     * The String used to represent a deliberately empty part of a
     * name.
     */
    String EMPTY_NAME = "";

    /**
     * Gets the unqualified name for this object.
     */
    String getName();

    /**
     * Sets the unqualified name for this object.
     */
    void setName(String name);

    /**
     * Gets the qualified name for this object. The qualified name
     * consists of the unqualified name of this object appended to the
     * qualified name of its namespace.
     */
    String getQualifiedName();

    /**
     * Gets the namespace for this object.
     */
    INamespace getNamespace();

    /**
     * Sets the namespace for this object.
     */
    void setNamespace(INamespace namespace);

}
