package org.suggs.fsm.behavior

import org.slf4j.LoggerFactory
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext
import org.suggs.fsm.execution.NamespaceObjectMapper

class PseudoState(name: String,
                  container: Namespace,
                  private val kind: PseudoStateKind)
    : Vertex(name, container) {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)!!
    }

    fun isInitialPseudoState(): Boolean {
        return kind == PseudoStateKind.INITIAL
    }

    override fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        if (outgoing.size != 1)
            throw IllegalStateException("Initial PseudoState must have exactly 1 outgoing transition, state [$name] has ${outgoing.size}")
        outgoing.first().fire(event, fsmExecutionContext)
    }

    override fun doEntryAction(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
    }

    override fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        log.debug("Exiting PseudoState ${deriveQualifiedName()}")
    }

    override fun doExitAction(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
    }
}
