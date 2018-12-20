package org.suggs.fsm.behavior

class Region(name: String,
             var vertices: Map<String, Vertex>,
             var transitions: Map<String, Transition>)
    : Namespace(name) {
}
