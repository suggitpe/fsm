package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Namespace
import org.suggs.fsm.behavior.State
import org.suggs.fsm.behavior.Vertex
import org.suggs.fsm.behavior.builders.EmptyBehaviourBuilder.Companion.anEmptyBehavior

class CompositeStateBuilder(name: String) :
        VertexBuilder(name) {
    lateinit var region: RegionBuilder

    fun withRegion(aRegion: RegionBuilder): VertexBuilder {
        region = aRegion
        return this
    }

    private val deferrableTriggers: MutableSet<TriggerBuilder> = HashSet()
    private var entryBehavior: BehaviorBuilder = anEmptyBehavior()
    private var exitBehavior: BehaviorBuilder = anEmptyBehavior()

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
        return State(name,
                container,
                deferrableTriggers.map { it.build() }.toSet(),
                entryBehavior.build(),
                exitBehavior.build())
    }

}
