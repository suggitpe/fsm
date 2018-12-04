/*
 * Namespace.java created on 04-Nov-2005 10:15:37 by billinro for project FsmLib
 *
 * Copyright (c) 2005 UBS Investment Bank
 */
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

    /**
     * The objects owned by this namespace
     */
    private Set ownedMembers_ = new HashSet();

    public Set getOwnedMembers() {
        return ownedMembers_;
    }

    public void setOwnedMembers(Set ownedMembers) {

        for (Object ownedMember : ownedMembers) {
            INamedElement e = (INamedElement) ownedMember;

            addOwnedMember(e);
        }

    }

    public void addOwnedMember(INamedElement newMember) {

        if (null == newMember) {
            String msg = "Null members are not permitted";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        for (Iterator iter = ownedMembers_.iterator(); iter.hasNext(); ) {
            INamedElement element = (INamedElement) iter.next();
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

//    /**
//     * Returns a String representation of this object using the
//     * default toString style.
//     */
//    @Override
//    public String toString() {
//        return "Namespace{" +
//                "ownedMembers_=" + ownedMembers_ +
//                '}';
//    }

}
