package org.suggs.fsm.execution

import org.suggs.fsm.behavior.Event

interface FsmStateManager {

    fun storeActiveState(state: String)

    fun getActiveState(businessObjectReference: BusinessObjectReference): String

    fun storeDeferredEvents(vararg events: Event)

    fun getDeferredEvents(): Set<Event>

    fun removeDeferredEvent(eventName: String)

}
