package org.suggs.fsm.behavior

import org.suggs.fsm.behavior.traits.Enterable
import org.suggs.fsm.behavior.traits.Exitable
import org.suggs.fsm.behavior.traits.Namespace

/**
 * Vertex is named element which is an abstraction of a node in a state machine graph. In general, it can be the
 * source or destination of any number of transitionBuilders.
 */
abstract class Vertex(name: String,
                      container: Namespace,
                      val incoming: MutableSet<Transition> = HashSet(),
                      val outgoing: MutableSet<Transition> = HashSet())
    : Enterable, Exitable, NamedElement(name, container){
    
        fun addOutgoingTransition(transition: Transition){
        outgoing.add(transition)
    }

    fun addIncomingTransition(transition: Transition){
        incoming.add(transition)
    }
}
