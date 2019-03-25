package org.suggs.fsm.stubs

import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.Event
import org.suggs.fsm.execution.BusinessObjectReference
import org.suggs.fsm.execution.FsmStateManager
import java.time.LocalDateTime

open class StubFsmStateManager : FsmStateManager {

    private var state: String = ""
    val deferredEvents = ArrayList<Event>()
    private val audits = ArrayList<Audit>()

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun storeDeferredEvents(vararg events: Event) {
        deferredEvents.addAll(events)
    }

    override fun getDeferredEvents(): Set<Event> {
        return deferredEvents.toSet()
    }

    override fun removeDeferredEvent(eventName: String) {
        deferredEvents.removeIf{ it.name == eventName}
    }

    override fun storeActiveState(state: String) {
        this.state = state
        audits.add(Audit(state))
        log.info("Recording state change to [$state]")
    }

    override fun getActiveState(businessObjectReference: BusinessObjectReference): String {
        return state
    }

    fun printAudits() {
        val buffer = StringBuffer("Transitions audit:\n")
        audits.map { buffer.append(" - ${it.timeStamp} - ${it.event}\n") }
        log.info(buffer.toString())
    }
}

data class Audit(val event: String){
    val timeStamp: LocalDateTime = LocalDateTime.now()
}