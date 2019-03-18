package org.suggs.fsm.behavior

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.traits.RegionContainer
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext
import org.suggs.fsm.execution.NamespaceObjectMapper

class StateMachine(name: String)
    : Behavior(name, {}), RegionContainer {

    lateinit var region: Region

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun registerMembersWithNamespace(namespaceContext: NamespaceObjectMapper) {
        region.registerWithNamespace(namespaceContext)
    }

    override fun findInheritedFireableTransitionsFor(event: BusinessEvent): Set<Transition> {
        return setOf()
    }

    override fun processEvent(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        log.debug("Ignoring state machine event processing")
    }


}