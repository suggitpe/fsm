package org.suggs.fsm.engine.uml2.kernel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.common.Assert;
import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.kernel.INamespace;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Namespace extends NamedElement implements INamespace {

    private static final Logger LOG = LoggerFactory.getLogger(Namespace.class);

    private Set<INamedElement> ownedMembers_ = new HashSet<>();

    public Set<INamedElement> getOwnedMembers() {
        return ownedMembers_;
    }

    public void setOwnedMembers(Set<INamedElement> ownedMembers) {
        for (INamedElement namedElement : ownedMembers) {
            addOwnedMember(namedElement);
        }
    }

    public void addOwnedMember(INamedElement newMember) {

        if (null == newMember) {
            String msg = "Null members are not permitted";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        for (Iterator<INamedElement> iter = ownedMembers_.iterator(); iter.hasNext(); ) {
            INamedElement element = iter.next();
            if (element.getName().equals(newMember.getName())) {
                // Replace the existing member and remove it from this
                // namespace
                element.setNamespace(null);
                iter.remove();
            }
        }

        // Set the namespace of the owned member
        newMember.setNamespace(this);
        ownedMembers_.add(newMember);
        Assert.state(ownedMembers_.contains(newMember));
    }

}
