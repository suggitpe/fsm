package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Event

data class EventBuilder(val name: String) {
    companion object {

        fun anEventCalled(name: String): EventBuilder {
            return EventBuilder(name)
        }
    }

    fun build(): Event {
        return Event(name)
    }
}