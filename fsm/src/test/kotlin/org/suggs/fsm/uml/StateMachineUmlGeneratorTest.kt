package org.suggs.fsm.uml

import org.junit.jupiter.api.Test
import org.suggs.fsm.behavior.builders.TestFsm
import org.suggs.fsm.behavior.builders.TestFsm.Companion.buildTestFsm
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.generateUmlFor
import org.suggs.fsm.uml.StateMachineUmlGenerator.Companion.writePumlToFile

class StateMachineUmlGeneratorTest {


    @Test
    fun `are describable in UML form`() {
        writePumlToFile(generateUmlFor(buildTestFsm()))
    }
}