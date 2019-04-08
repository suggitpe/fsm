package org.suggs.fsm.behavior.traits

import org.suggs.fsm.behavior.Transition
import org.suggs.fsm.execution.BusinessEvent

interface RegionContainer : Namespace, Processable {

    fun findInheritedFireableTransitionsFor(event: BusinessEvent): Set<Transition>

}