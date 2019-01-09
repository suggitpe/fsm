package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Event

class EventBuilder(val name: String)
    : Builder<Event> {
    companion object {

        fun anEventCalled(name: String): EventBuilder {
            return EventBuilder(name)
        }
    }

    override fun build(): Event {
        return Event(name)
    }
}