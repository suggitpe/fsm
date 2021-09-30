package org.suggs.fsm.behavior

class Event(name: String)
    : NamedElement(name) {

    companion object {
        const val COMPLETION_EVENT_NAME = "\$COMPLETION"
    }

}