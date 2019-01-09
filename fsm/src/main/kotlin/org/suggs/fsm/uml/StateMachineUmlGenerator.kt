package org.suggs.fsm.uml

import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.Region
import org.suggs.fsm.behavior.StateMachine
import java.io.File
import java.lang.StringBuilder

class StateMachineUmlGenerator {


    companion object {

        private val log = LoggerFactory.getLogger(this::class.java)


        fun generateUmlFor(stateMachine: BehavioredClassifier): String {
            return """
                |@startuml
                |scale 250 width
                |skinparam backgroundColor LightYellow
                |skinparam state {
                |StartColor MediumBlue
                |EndColor Red
                |BackgroundColor Peru
                |BackgroundColor<<Warning>> Olive
                |BorderColor Gray
                |FontName Impact
                |}
                |
                |${generateUmlFor(stateMachine.ownedBehavior as StateMachine)}
                |
                |@enduml""".trimMargin()
        }

        fun generateUmlFor(machine: StateMachine): String{
            return generateUmlFor(machine.region)
        }

        private fun generateUmlFor(region: Region): String {
            val puml = StringBuilder()

            puml.append("\n' states ....")
            for(state in region.vertices.values){
                puml.append("\n${UmlSyntaxHack.createUmlDefinitionFor(state)}")
            }

            puml.append("\n\n' transitions ...")
            for(transition in region.transitions.values){
                puml.append("\n${UmlSyntaxHack.createUmlSyntaxFor(transition)}")
            }

            return puml.toString()
        }

        fun writePumlToFile(puml: String, filename: String = "fsm.puml") {
            return File(buildFileForOutput(filename)).bufferedWriter().use { out -> out.write(puml) }
        }

        private fun buildFileForOutput(filename: String): String {
            val dir = System.getenv("puml.output.dir") ?: "build/puml"
            log.info("Writing puml to directory [$dir]")
            if (!File(dir).exists()) {
                if (!File(dir).mkdirs()) throw IllegalStateException("Failed to create directory [$dir]")
            }
            return "$dir/$filename"
        }
    }
}