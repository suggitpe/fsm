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

    @Test fun `describes simple state machines`() {
        writePumlToFile(generateUmlFor(simpleFsmPrototype().build()), "simpleFsm.puml")
    }

    @Test fun `describes simple state machines with two routes`() {
        writePumlToFile(generateUmlFor(fsmWithTwoOutcomesPrototype().build()), "simpleFsmLeftAndRight.puml")
    }

    @Test fun `describes state machines with deferred events`() {
        writePumlToFile(generateUmlFor(fsmWithDeferredTransitionsPrototype().build()), "deferredTransitions.puml")
    }

    @Test fun `describes state machine with deferred and automated transitions`() {
        writePumlToFile(generateUmlFor(fsmWithDeferredAndAutomatedTransitionsPrototype().build()), "deferredAndAutomatedTransitions.puml")
    }

    @Test fun `describes a simple composite State machine`() {
        writePumlToFile(generateUmlFor(simpleNestedStatemachineProtoType { true }.build()), "simpleCompositeFsm.puml")
    }

    @Test fun `describes composite state machines`() {
        writePumlToFile(generateUmlFor(nestedStateStateMachinePrototype().build()), "compositeFsm.puml")
    }

    @Test fun `describes a composite state machine with guard condition to close`() {
        writePumlToFile(generateUmlFor(nestedStateStateMachineWithGuardExitPrototype { true }.build()), "compositeWithGuardExitFsm.puml")
    }

    @Test fun `describes state machines that have states with entry and exit behaviors`() {
        writePumlToFile(generateUmlFor(fsmWithEntryAndExitBehaviorsPrototype().build()), "entryAndExitBehaviors.puml")
    }

}