package org.suggs.fsm.behavior

import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.PseudoStateKind.*
import org.suggs.fsm.behavior.traits.Namespace
import org.suggs.fsm.execution.BusinessEvent
import org.suggs.fsm.execution.FsmExecutionContext

/**
 * Contains the PseudoState class plus the associated behavioral classes to
 * support the different pseudo state types.
 */
class PseudoState(name: String,
                  container: Namespace,
                  private val kind: PseudoStateKind)
    : Vertex(name, container) {

    private var behavior: PseudoStateBehaviour = when (kind) {
        INITIAL -> InitialPseudoStateBehavior()
        ENTRY_POINT -> EntryPointPseudoStateBehavoir()
        EXIT_POINT -> ExitPointPseudoStateBehavoir()
        else -> throw IllegalStateException("Trying to initialise unsupported PseudoStateBehaviour")
    }

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)!!
    }

    fun isInitialPseudoState(): Boolean {
        return kind == INITIAL
    }

    override fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        behavior.enter(event, fsmExecutionContext)
    }

    override fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
        behavior.exit(event, fsmExecutionContext)
    }

    override fun doEntryAction(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
    }

    override fun doExitAction(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
    }

    interface PseudoStateBehaviour {
        fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)
        fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext)
    }

    inner class InitialPseudoStateBehavior : PseudoStateBehaviour {

        override fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
            if (outgoing.size != 1)
                throw IllegalStateException("Initial PseudoState must have exactly 1 outgoing transition, state [$name] has ${outgoing.size}")
            outgoing.first().fire(event, fsmExecutionContext)
        }

        override fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
            log.debug("Exiting PseudoState ${deriveQualifiedName()}")
        }
    }

    inner class EntryPointPseudoStateBehavoir : PseudoStateBehaviour {
        override fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
            throw NotImplementedError()
        }

        override fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
            throw NotImplementedError()
        }
    }

    inner class ExitPointPseudoStateBehavoir : PseudoStateBehaviour {
        override fun enter(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
            throw NotImplementedError()
        }

        override fun exit(event: BusinessEvent, fsmExecutionContext: FsmExecutionContext) {
            throw NotImplementedError()
        }
    }
}
