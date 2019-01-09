package org.suggs.fsm.uml

import org.suggs.fsm.behavior.*

class UmlSyntaxHack {

    companion object {

        fun createUmlDefinitionFor(vertex: Vertex): String {
            return when (vertex) {
                is FinalState -> ""
                is PseudoState -> ""
                is State -> {
                    """State ${vertex.name} { ${createEntryBehaviorFor(vertex)} ${createExitBehaviorFor(vertex)}
                        |}
                        |
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


        fun createUmlSyntaxFor(element: NamedElement): String {
            return when (element) {
                is Transition -> {
                    "${createUmlSyntaxFor(element.source)} --> ${createUmlSyntaxFor(element.target)} ${addTriggersFor(element)}"
                }
                is PseudoState -> "[*]"
                is FinalState -> "[*]"
                is State -> element.name
                else -> ""
            }
        }

        private fun addTriggersFor(transition: Transition): String {
            if (transition.triggers.isNotEmpty()) {
                return ": ${transition.triggers.first().event.name}"
            }
            return ""
        }

    }

}