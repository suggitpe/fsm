package org.suggs.fsm.execution

import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.BehaviouredClassifier
import org.suggs.fsm.behavior.NamedElement
import org.suggs.fsm.behavior.State

class NamespaceObjectMapper {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)!!
    }

    private val namedElements: MutableMap<String, NamedElement> = HashMap()

    fun initialise(stateMachineDefinition: BehaviouredClassifier) {
        log.debug("Initialising namespace context with named elements in the FSM")
        stateMachineDefinition.registerWithNamespace(this)
    }

    fun registerElement(namedElement: NamedElement) {
        namedElements[namedElement.deriveQualifiedName()] = namedElement
    }

    fun retrieveStateCalled(activeStateName: String): State? {
        return namedElements[activeStateName] as State
    }

}
