package org.suggs.fsm.uml

import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.*
import java.io.File
import java.lang.StringBuilder

class StateMachineUmlGenerator {


    companion object {

        private val log = LoggerFactory.getLogger(this::class.java)


        fun generateUmlFor(stateMachine: BehavioredClassifier): String {
            return """
                |@startuml
                |skinparam backgroundColor LightYellow
                |skinparam state {
                |StartColor MediumBlue
                |EndColor Green
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
                puml.append("\n${createUmlDefinitionFor(state, region.name)}")
            }

            puml.append("\n\n' transitions ...")
            for(transition in region.transitions.values){
                puml.append("\n${createUmlSyntaxFor(transition, region.name)}")
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

        fun createUmlDefinitionFor(vertex: Vertex, prefix: String): String {
            return when (vertex) {
                is FinalState -> ""
                is PseudoState -> ""
                is SimpleState -> {
                    """State ${prefix}_${vertex.name} { ${createEntryBehaviorFor(vertex)} ${createExitBehaviorFor(vertex)}
                        |}
                    """.trimMargin()
                }
                is CompositeState -> {
                    """State ${prefix}_${vertex.name} { ${createEntryBehaviorFor(vertex)} ${createExitBehaviorFor(vertex)}
                        | ${generateUmlFor(vertex.region)}
                        |}
                    """.trimMargin()
                }
                else -> ""
            }
        }

        private fun createEntryBehaviorFor(vertex: State): String {
            return if (vertex.entryBehavior.name == "EMPTY") ""
            else "\n   ${vertex.name} : entry/ ${vertex.entryBehavior.name}"
        }

        private fun createExitBehaviorFor(vertex: State): String {
            return if (vertex.exitBehavior.name == "EMPTY") ""
            else "\n   ${vertex.name} : entry/ ${vertex.exitBehavior.name}"
        }


        fun createUmlSyntaxFor(element: NamedElement, prefix: String): String {
            return when (element) {
                is Transition -> {
                    "${createUmlSyntaxFor(element.source, prefix)} --> ${createUmlSyntaxFor(element.target, prefix)} ${addTriggersFor(element)}"
                }
                is PseudoState -> "[*]"
                is FinalState -> "[*]"
                is State -> "${prefix}_${element.name}"
                else -> ""
            }
        }

        private fun addTriggersFor(transition: Transition): String {
            if (transition.triggers.isNotEmpty()
                    && transition.triggers.first().name != Event.COMPLETION_EVENT_NAME) {
                return ": ${transition.triggers.first().event.name}"
            }
            return ""
        }
    }
}