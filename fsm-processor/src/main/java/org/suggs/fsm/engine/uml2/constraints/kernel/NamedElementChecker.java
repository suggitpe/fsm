package org.suggs.fsm.engine.uml2.constraints.kernel;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.kernel.INamespace;

public class NamedElementChecker implements INamedElementChecker {

    public void checkConstraints(INamedElement namedElement) {

        /*
         * [1] If there is no name, or one of the containing
         * namespaces has no name, there is no qualified name.
         */
        if (INamedElement.EMPTY_NAME.equals(namedElement.getName())) {
            Assert.state(INamedElement.EMPTY_NAME.equals(namedElement.getQualifiedName()),
                    "Non-empty qualified name found for element with empty name " + namedElement);
        }

        // Recurse up containing namespaces until we reach one that's
        // null, i.e the top state
        for (INamespace namespace = namedElement.getNamespace(); null != namespace; ) {
            if (INamedElement.EMPTY_NAME.equals(namespace.getName())) {
                Assert.state(INamedElement.EMPTY_NAME.equals(namedElement.getQualifiedName()),
                        "Non-empty qualified name found for element with empty parent namespace name " + namedElement);
            }
            namespace = namespace.getNamespace();
        }

        /*
         * [2] When there is a name, and all of the containing
         * namespaces have a name, the qualified name is constructed
         * from the names of the containing namespaces.
         */

        if (!INamespace.EMPTY_NAME.equals(namedElement.getQualifiedName())) {
            // Recurse up containing namespaces until we reach one
            // that's
            // null, i.e the top state
            String targetQualifiedName = namedElement.getName();

            for (INamespace namespace = namedElement.getNamespace(); null != namespace; ) {
                targetQualifiedName = namespace.getName() + INamespace.NAMESPACE_SEPARATOR + targetQualifiedName;
                namespace = namespace.getNamespace();
            }

            targetQualifiedName = INamespace.TOP_LEVEL_NAMESPACE + INamespace.NAMESPACE_SEPARATOR + targetQualifiedName;

            Assert.state(namedElement.getQualifiedName().equals(targetQualifiedName), "Qualified name "
                    + namedElement.getQualifiedName()
                    + " is incorrect for named element "
                    + namedElement);
        }

        /*
         * [3] If a NamedElement is not owned by a Namespace, it does
         * not have a visibility.
         */

    }
}
