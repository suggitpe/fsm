package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.NamedElementContainer
import org.suggs.fsm.behavior.PseudoStateKind.*
import org.suggs.fsm.behavior.Vertex

abstract class VertexBuilder(val name: String) {
    companion object {

        fun anInitialPseudoStateCalled(name: String): PseudoStateBuilder {
            return PseudoStateBuilder(name, INITIAL)
        }

        fun anEntryPointPseudoStateCalled(name: String): PseudoStateBuilder {
            return PseudoStateBuilder(name, ENTRY_POINT)
        }

        fun anExitPointPseudoStateCalled(name: String): PseudoStateBuilder {
            return PseudoStateBuilder(name, EXIT_POINT)
        }

        fun aSimpleStateCalled(name: String): SimpleStateBuilder {
            return SimpleStateBuilder(name)
        }

        fun aCompositeStateCalled(name: String): CompositeStateBuilder {
            return CompositeStateBuilder(name)
        }

        fun aFinalStateCalled(name: String): FinalStateBuilder {
            return FinalStateBuilder(name)
        }

    }

    abstract fun withDeferrableTriggers(vararg newTriggers: TriggerBuilder): VertexBuilder
    abstract fun withEntryBehavior(behavior: BehaviorBuilder): VertexBuilder
    abstract fun withExitBehavior(behavior: BehaviorBuilder): VertexBuilder
    abstract fun build(container: NamedElementContainer): Vertex

}
