package org.suggs.fsm.behavior.traits

interface Named {

    val name: String

    fun deriveQualifiedName(): String
}