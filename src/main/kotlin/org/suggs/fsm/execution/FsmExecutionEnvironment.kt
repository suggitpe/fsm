package org.suggs.fsm.execution

import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.BehaviouredClassifier
import org.suggs.fsm.behavior.State.Companion.TRANSITIONING
import org.suggs.fsm.behavior.StateMachine

class FsmExecutionEnvironment(private val stateMachineDefinition: BehaviouredClassifier,
                              private val fsmExecutionContext: FsmExecutionContext) {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)!!
    }

    init{
        fsmExecutionContext.initialiseWith(stateMachineDefinition)
    }

    fun handleEvent(event: BusinessEvent) {

        log.debug("Handling event $event")
        var activeStateName = fsmExecutionContext.stateManager.getActiveState()

        if (activeStateName.isBlank()) {
            log.debug("No active state exists for ${event.identifier}, initialising to pseudostate for top region")
            var initialState = (stateMachineDefinition.ownedBehavior as StateMachine).region.getInitialState()
            fsmExecutionContext.stateManager.storeActiveState(initialState.deriveQualifiedName())
            initialState.enter(event, fsmExecutionContext)
            activeStateName = fsmExecutionContext.stateManager.getActiveState()
        } else if (activeStateName.endsWith(TRANSITIONING)) {
            throw IllegalStateException("The current active state is in an incomplete state")
        }

        var currentState = fsmExecutionContext.namespaceContext.retrieveStateCalled(activeStateName)
        currentState!!.processEvent(event, fsmExecutionContext)

        activeStateName = fsmExecutionContext.stateManager.getActiveState()
        checkTransitionedIntoValidState(activeStateName, event, fsmExecutionContext.namespaceContext)
    }

    private fun checkTransitionedIntoValidState(activeStateName: String, event: BusinessEvent, namespaceContext: NamespaceObjectMapper){
        if(activeStateName.isBlank() || namespaceContext.retrieveStateCalled(activeStateName) == null){
            throw IllegalStateException("After processing we have entered state [$activeStateName], but this is an unregistered state")
        }
        if(activeStateName == TRANSITIONING){
            throw IllegalStateException("After processing event [${event.type}] we are left in an inconsistent state [$TRANSITIONING]")
        }
    }
}