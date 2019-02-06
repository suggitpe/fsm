package org.suggs.fsm.behavior

class Event(name: String)
    : NamedElement(name){

    companion object {
        const val COMPLETION_EVENT_NAME = "\$COMPLETION"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }


}