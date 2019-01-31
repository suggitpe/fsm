package org.suggs.fsm.behavior

abstract class Namespace(name: String,
                     val ownedMembers: Set<NamedElement> = HashSet())
    : NamedElement(name)
