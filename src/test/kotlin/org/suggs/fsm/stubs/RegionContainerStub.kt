package org.suggs.fsm.stubs

import org.suggs.fsm.behavior.NamedElementContainer
import org.suggs.fsm.behavior.Transition
import org.suggs.fsm.behavior.traits.RegionContainer
import org.suggs.fsm.execution.BusinessEvent

class RegionContainerStub(name: String) : NamedElementContainer(name, setOf()), RegionContainer {

    companion object {
        fun aRegionContainerStub(): RegionContainerStub {
            return RegionContainerStub("context")
        }
    }

    override fun findInheritedFireableTransitionsFor(event: BusinessEvent): Set<Transition> {
        return setOf()
    }
}