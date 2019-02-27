package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.PseudoState
import org.suggs.fsm.behavior.PseudoStateKind
import org.suggs.fsm.behavior.Region

class PseudoStateBuilder(name: String,
                         val pseudoStateKind: PseudoStateKind)
    : VertexBuilder(name) {

    constructor(pseudoStateKind: PseudoStateKind) : this(pseudoStateKind.toString(), pseudoStateKind)

    override fun withDeferrableTriggers(vararg newTriggers: TriggerBuilder): VertexBuilder {
        throw IllegalStateException("You cannot define deferrable triggers on a pseudo state")
    }

    override fun withEntryBehavior(behavior: BehaviorBuilder): VertexBuilder {
        throw IllegalStateException("You cannot define entry and exit behaviors on pseudo states")
    }

    override fun withExitBehavior(behavior: BehaviorBuilder): VertexBuilder {
        throw IllegalStateException("You cannot define entry and exit behaviors on pseudo states")
    }

    override fun build(container: Region): PseudoState {
        return PseudoState(name, container, pseudoStateKind)
    }


}
