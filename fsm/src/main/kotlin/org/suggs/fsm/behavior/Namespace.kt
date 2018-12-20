package org.suggs.fsm.behavior

open class Namespace(name: String,
                val ownedMembers: Set<NamedElement> = HashSet())
    : NamedElement(name){

    override fun umlSyntax(): String =""
}
