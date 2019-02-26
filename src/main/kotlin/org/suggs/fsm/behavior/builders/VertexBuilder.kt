package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.PseudoStateKind.*
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.traits.Namespace

abstract class VertexBuilder(val name: String) {
    companion object {

        fun anInitialPseudoStateCalled(name: String): PseudoStateBuilder {
            return PseudoStateBuilder(name, INITIAL)
        }

        fun anInitialPseudoState(): PseudoStateBuilder{
            return PseudoStateBuilder(INITIAL)
        }

        fun anEntryPointPseudoState(): PseudoStateBuilder {
            return PseudoStateBuilder(ENTRY_POINT)
        }

        fun anExitPointPseudoState(): PseudoStateBuilder {
            return PseudoStateBuilder(EXIT_POINT)
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

        fun aFinalState(): FinalStateBuilder{
            return FinalStateBuilder()
        }

    }

    abstract fun withDeferrableTriggers(vararg newTriggers: TriggerBuilder): VertexBuilder
    abstract fun withEntryBehavior(behavior: BehaviorBuilder): VertexBuilder
    abstract fun withExitBehavior(behavior: BehaviorBuilder): VertexBuilder
    abstract fun build(container: Namespace): Vertex

}
