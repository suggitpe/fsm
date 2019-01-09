package org.suggs.fsm.behavior

class Transition(name: String,
                 val source: Vertex,
                 val target: Vertex,
                 val triggers: Set<Trigger> = HashSet(),
                 val effects: Set<Behavior> = HashSet())
    : Namespace(name)