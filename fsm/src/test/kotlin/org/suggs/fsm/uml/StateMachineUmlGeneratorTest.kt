package org.suggs.fsm.uml

import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.FsmPrototypes.fsmWithDeferredAutomatedTransitionsPrototype
import org.suggs.fsm.behavior.builders.FsmPrototypes.fsmWithEntryAndExitBehaviorsPrototype
import org.suggs.fsm.behavior.builders.FsmPrototypes.nestedStateStateMachinePrototype
import org.suggs.fsm.behavior.builders.FsmPrototypes.simpleStateMachinePrototype
import org.suggs.fsm.behavior.builders.FsmPrototypes.simpleStateMachineWithTwoOutcomesPrototype
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.generateUmlFor
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.writePumlToFile

class StateMachineUmlGeneratorTest {

    @Test
    fun `describes simple state machines`() {
        writePumlToFile(generateUmlFor(simpleStateMachinePrototype().build()), "simpleFsm.puml")
    }

    @Test
    fun `describes simple state machines with two routes`() {
        writePumlToFile(generateUmlFor(simpleStateMachineWithTwoOutcomesPrototype().build()), "simpleFsmLeftAndRight.puml")
    }

    @Test
    fun `describes composite state machines`() {
        writePumlToFile(generateUmlFor(nestedStateStateMachinePrototype().build()), "compositeFsm.puml")
    }

    @Test
    fun `describes state machines with triggers and effects`() {
        writePumlToFile(generateUmlFor(fsmWithDeferredAutomatedTransitionsPrototype().build()), "automatedTransitions.puml")
    }

    @Test
    fun `describes state machines that have states with entry and exit behaviors`() {
        writePumlToFile(generateUmlFor(fsmWithEntryAndExitBehaviorsPrototype().build()), "entryAndExitBehaviors.puml")
    }
}