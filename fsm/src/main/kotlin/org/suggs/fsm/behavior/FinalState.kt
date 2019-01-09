package org.suggs.fsm.behavior

class FinalState(name: String,
                 entryBehavior: Behavior,
                 exitBehavior: Behavior)
    : State(name, HashSet(), entryBehavior, exitBehavior)