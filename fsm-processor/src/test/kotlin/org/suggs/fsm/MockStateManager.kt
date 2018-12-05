package org.suggs.fsm

import org.apache.commons.logging.LogFactory
import org.suggs.fsm.framework.spi.IStateManager
import org.suggs.fsm.uml2.scribe.runtime.IEventContext
import java.util.*

class MockStateManager : IStateManager {

    private val log = LogFactory.getLog(MockStateManager::class.java)
    private var activeState: String? = null
    private var historyStates: Map<String, String> = HashMap()
    private var deferredEvents: List<IEventContext> = ArrayList()

    override fun storeActiveState(activeState: String) {
        this.activeState = activeState
    }

    override fun getActiveState(): String? {
        return activeState
    }

    override fun getHistoryStates(): Map<String, String> {
        return historyStates
    }

    override fun storeHistoryStates(historyStateMap: Map<String, String>) {
        historyStates = historyStateMap
        log.debug("Storing history states: $historyStateMap")
    }

    override fun storeDeferredEvents(eventList: List<IEventContext>) {
        deferredEvents = eventList
        log.debug("Storing deferred events: $deferredEvents")
    }

    override fun getDeferredEvents(): List<IEventContext> {
        return deferredEvents
    }


}