package org.suggs.fsm.execution

import org.suggs.fsm.behavior.BehaviouredClassifier

data class FsmExecutionContext(val stateManager: FsmStateManager) {

    val namespaceContext: NamespaceObjectMapper = NamespaceObjectMapper()

    fun initialiseWith(stateMachineDefinition: BehaviouredClassifier) {
            namespaceContext.initialise(stateMachineDefinition)
    }

}