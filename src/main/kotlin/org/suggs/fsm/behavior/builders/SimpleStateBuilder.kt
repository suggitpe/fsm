package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.SimpleState
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.builders.EmptyBehaviourBuilder.Companion.anEmptyBehavior
import org.suggs.fsm.behavior.traits.Namespace

open class SimpleStateBuilder(name: String)
    : VertexBuilder(name) {

    companion object {
        fun aStateCalled(name: String): SimpleStateBuilder {
            return SimpleStateBuilder(name)
        }
    }

    protected val deferrableTriggers: MutableSet<TriggerBuilder> = HashSet()
    protected var entryBehavior: BehaviorBuilder = anEmptyBehavior()
    protected var exitBehavior: BehaviorBuilder = anEmptyBehavior()

    override fun withDeferrableTriggers(vararg newTriggers: TriggerBuilder): VertexBuilder {
        deferrableTriggers.addAll(newTriggers)
        return this
    }

    override fun withEntryBehavior(behavior: BehaviorBuilder): VertexBuilder {
        entryBehavior = behavior
        return this
    }

    override fun withExitBehavior(behavior: BehaviorBuilder): VertexBuilder {
        exitBehavior = behavior
        return this
    }

    override fun build(container: Namespace): Vertex {
        return SimpleState(name,
                container,
                deferrableTriggers.map { it.build() }.toSet(),
                entryBehavior.build(),
                exitBehavior.build())
    }

}
