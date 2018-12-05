package org.suggs.fsm.engine.uml2.constraints.kernel;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.kernel.INamespace;

import java.util.HashSet;
import java.util.Set;

public class NamespaceChecker extends NamedElementChecker implements INamespaceChecker {

    public void checkConstraints(INamespace namespace) {

        Set<String> names = new HashSet<>();

        Set<INamedElement> namedElements = namespace.getOwnedMembers();
        for (INamedElement ownedMember : namedElements) {
            names.add(ownedMember.getName());
        }

        Assert.state(names.size() == namedElements.size(), "There were " + names.size() + " distinct names for "
                + namedElements.size() + " owned members");
    }
}
