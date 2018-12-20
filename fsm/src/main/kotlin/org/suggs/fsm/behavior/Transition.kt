package org.suggs.fsm.behavior

class Transition(name: String,
                 val source: Vertex,
                 val target: Vertex,
                 val triggers: MutableSet<Trigger> = HashSet() )
    : Namespace(name) {

    fun addTriggers(newTriggers: Set<Trigger>){
        triggers.addAll(newTriggers)
    }

}