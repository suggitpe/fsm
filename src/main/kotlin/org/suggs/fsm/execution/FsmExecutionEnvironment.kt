package org.suggs.fsm.execution

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.State

class FsmExecutionEnvironment(private val stateMachineDefinition: BehavioredClassifier,
                              private val fsmExecutionContext: FsmExecutionContext) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    init {
        fsmExecutionContext.initialiseWith(stateMachineDefinition)
    }

    fun handleEvent(event: BusinessEvent) {
        log.debug("Handling event $event")
        var activeStateName = fsmExecutionContext.stateManager.getActiveState(event.identifier)

        if (activeStateName.isBlank()) {
            log.debug("No active state exists for ${event.identifier}, initialising to PseudoState for top region")
            var initialState = stateMachineDefinition.stateMachine.region.getInitialState()
            fsmExecutionContext.stateManager.storeActiveState(initialState.deriveQualifiedName())
            initialState.enter(event, fsmExecutionContext)
            activeStateName = fsmExecutionContext.stateManager.getActiveState(event.identifier)
        } else if (activeStateName.endsWith(State.TRANSITIONING)) {
            throw IllegalStateException("The current active state is in an incomplete state")
        }

        var currentState = fsmExecutionContext.namespaceContext.retrieveStateCalled(activeStateName)
        currentState!!.processEvent(event, fsmExecutionContext)

        activeStateName = fsmExecutionContext.stateManager.getActiveState(event.identifier)
        checkTransitionedIntoValidState(activeStateName, event, fsmExecutionContext.namespaceContext)
    }

    private fun checkTransitionedIntoValidState(activeStateName: String, event: BusinessEvent, namespaceContext: NamespaceObjectMapper) {
        if (activeStateName.isBlank() || namespaceContext.retrieveStateCalled(activeStateName) == null) {
            throw IllegalStateException("After processing we have entered state [$activeStateName], but this is an unregistered state")
        }

        if (activeStateName == State.TRANSITIONING) {
            throw IllegalStateException("After processing event [${event.type}] we are left in an inconsistent state [${State.TRANSITIONING}]")
        }
    }


}