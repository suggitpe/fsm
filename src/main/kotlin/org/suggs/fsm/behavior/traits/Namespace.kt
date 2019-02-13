package org.suggs.fsm.behavior.traits

import org.suggs.fsm.behavior.NamedElement

interface Namespace : Named {

    val ownedMembers: Set<NamedElement>

}