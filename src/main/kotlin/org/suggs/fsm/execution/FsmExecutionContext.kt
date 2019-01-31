package org.suggs.fsm.execution

import org.suggs.fsm.behavior.BehavioredClassifier

data class FsmExecutionContext(val stateManager: FsmStateManager) {

    val namespaceContext: NamespaceObjectMapper = NamespaceObjectMapper()

    fun initialiseWith(stateMachineDefinition: BehavioredClassifier) {
            namespaceContext.initialise(stateMachineDefinition)
    }

}