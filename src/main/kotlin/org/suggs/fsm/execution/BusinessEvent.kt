package org.suggs.fsm.execution

data class BusinessEvent(val type: String,
                         val identifier: BusinessObjectReference) {

    override fun toString(): String {
        return "BusinessEvent(type='$type', identifier=$identifier)"
    }
}
