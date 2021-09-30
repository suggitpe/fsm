package org.suggs.fsm.behavior

import org.suggs.fsm.behavior.traits.Namespace

abstract class NamedElementContainer(name: String,
                                     namespace: Namespace?,
                                     override val ownedMembers: Set<NamedElement> = HashSet())
    : NamedElement(name, namespace), Namespace {

    constructor(name: String, ownedMembers: Set<NamedElement> = HashSet())
            : this(name, null, ownedMembers)

}
