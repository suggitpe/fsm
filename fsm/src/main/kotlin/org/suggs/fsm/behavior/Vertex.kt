package org.suggs.fsm.behavior

/**
 * Vertex is named element which is an abstraction of a node in a state machine graph. In general, it can be the
 * source or destination of any number of transitionBuilders.
 */
abstract class Vertex(name: String,
                      val incoming: MutableSet<Transition> = HashSet(),
                      val outgoing: MutableSet<Transition> = HashSet())
    : NamedElement(name){

    fun addOutgoingTransition(transition: Transition){
        outgoing.add(transition)
    }

    fun addIncomingTransition(transition: Transition){
        incoming.add(transition)
    }
}
