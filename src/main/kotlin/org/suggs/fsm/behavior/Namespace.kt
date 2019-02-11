package org.suggs.fsm.behavior

abstract class Namespace(name: String,
                         namespace: Namespace?,
                         val ownedMembers: Set<NamedElement> = HashSet())
    : NamedElement(name, namespace) {

    constructor(name: String, ownedMembers: Set<NamedElement> = HashSet()) : this(name, null, ownedMembers)

}
