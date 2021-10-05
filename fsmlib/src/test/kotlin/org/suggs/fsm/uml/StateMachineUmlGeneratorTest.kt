package org.suggs.fsm.uml

import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.FsmPrototypes.fsmWithEntryAndExitBehaviorsPrototype
import org.suggs.fsm.behavior.builders.FsmPrototypes.nestedStateStateMachinePrototype
import org.suggs.fsm.execution.CompositeStateTransitionTest.Companion.simpleNestedStatemachineProtoType
import org.suggs.fsm.execution.CompositeStateUsingGuardActionToExit.Companion.nestedStateStateMachineWithGuardExitPrototype
import org.suggs.fsm.execution.DeferredEventsTest.Companion.fsmWithDeferredAndAutomatedTransitionsPrototype
import org.suggs.fsm.execution.DeferredEventsTest.Companion.fsmWithDeferredTransitionsPrototype
import org.suggs.fsm.execution.FsmExecutionEnvironmentTest.Companion.simpleFsmPrototype
import org.suggs.fsm.execution.SimpleStateMachineNavigation.Companion.fsmWithTwoOutcomesPrototype
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.generateUmlFor
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.writePumlToFile

class StateMachineUmlGeneratorTest {

    @Test
    fun `describes simple state machines`() {
        simpleFsmPrototype().build()
            .let { generateUmlFor(it) }
            .let { writePumlToFile(it, "simpleFsm.puml") }
    }

    @Test
    fun `describes simple state machines with two routes`() {
        fsmWithTwoOutcomesPrototype().build()
            .let { generateUmlFor(it) }
            .let { writePumlToFile(it, "simpleFsmLeftAndRight.puml") }
    }

    @Test
    fun `describes state machines with deferred events`() {
        fsmWithDeferredTransitionsPrototype().build()
            .let { generateUmlFor(it) }
            .let { writePumlToFile(it, "deferredTransitions.puml") }
    }

    @Test
    fun `describes state machine with deferred and automated transitions`() {
        fsmWithDeferredAndAutomatedTransitionsPrototype().build()
            .let { generateUmlFor(it) }
            .let { writePumlToFile(it, "deferredAndAutomatedTransitions.puml") }
    }

    @Test
    fun `describes a simple composite State machine`() {
        simpleNestedStatemachineProtoType { true }.build()
            .let { generateUmlFor(it) }
            .let { writePumlToFile(it, "simpleCompositeFsm.puml") }
    }

    @Test
    fun `describes composite state machines`() {
        nestedStateStateMachinePrototype().build()
            .let { generateUmlFor(it) }
            .let { writePumlToFile(it, "compositeFsm.puml") }
    }

    @Test
    fun `describes a composite state machine with guard condition to close`() {
        nestedStateStateMachineWithGuardExitPrototype { true }.build()
            .let { generateUmlFor(it) }
            .let { writePumlToFile(it, "compositeWithGuardExitFsm.puml") }
    }

    @Test
    fun `describes state machines that have states with entry and exit behaviors`() {
        fsmWithEntryAndExitBehaviorsPrototype().build()
            .let { generateUmlFor(it) }
            .let { writePumlToFile(it, "entryAndExitBehaviors.puml") }
    }

}