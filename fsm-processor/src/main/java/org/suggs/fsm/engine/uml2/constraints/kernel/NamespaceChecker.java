package org.suggs.fsm.engine.uml2.constraints.kernel;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.kernel.INamespace;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NamespaceChecker extends NamedElementChecker implements INamespaceChecker {

    public void checkConstraints(INamespace namespace) {

        {
            /*
             * Owned members of the namespace must be distinguishable.
             * We check the names for uniqueness.
             */
            Set names = new HashSet();

            Set namedElements = namespace.getOwnedMembers();
            for (Iterator namedElementIt = namedElements.iterator(); namedElementIt.hasNext(); ) {
                INamedElement ownedMember = (INamedElement) namedElementIt.next();
                names.add(ownedMember.getName());
            }

            Assert.state(names.size() == namedElements.size(), "There were " + names.size() + " distinct names for "
                    + namedElements.size() + " owned members");

        }

    }
}
