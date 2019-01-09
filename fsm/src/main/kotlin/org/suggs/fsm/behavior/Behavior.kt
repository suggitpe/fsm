package org.suggs.fsm.behavior

open class Behavior(name: String,
                    private val executableFunction: (EventContext) -> Unit)
    : Namespace(name) {

    fun execute(context: EventContext) {
        executableFunction(context)
    }

}

class EmptyBehavior : Behavior("EMPTY", {})
