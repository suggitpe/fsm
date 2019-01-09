package org.suggs.fsm.uml

import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.TestFsm.Companion.buildFsmWithAutomatedTransitions
import org.suggs.fsm.behavior.builders.TestFsm.Companion.buildFsmWithEntryAndExitBehaviors
import org.suggs.fsm.behavior.builders.TestFsm.Companion.buildNestedStateStateMachine
import org.suggs.fsm.behavior.builders.TestFsm.Companion.buildSimpleStateMachine
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.generateUmlFor
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.writePumlToFile

class StateMachineUmlGeneratorTest {

    @Test
    fun `describes simple state machines`() {
        writePumlToFile(generateUmlFor(buildSimpleStateMachine()), "simpleFsm.puml")
    }

    @Test
    fun `describes composite state machines`(){
        writePumlToFile(generateUmlFor(buildNestedStateStateMachine()), "compositeFsm.puml")
    }

    @Test
    fun `describes state machines with triggers and effects`(){
        writePumlToFile(generateUmlFor(buildFsmWithAutomatedTransitions()), "automatedTransitions.puml")
    }

    @Test
    fun `describes state machines that have states with entry and exit behaviors`(){
        writePumlToFile(generateUmlFor(buildFsmWithEntryAndExitBehaviors()), "entryAndExitBehaviors.puml")
    }
}