package org.suggs.fsm.behavior

open class State(name: String,
                 val deferrableTriggers: Set<Trigger>,
                 val entryBehavior: Behavior,
                 val exitBehavior: Behavior)
    : Vertex(name)
