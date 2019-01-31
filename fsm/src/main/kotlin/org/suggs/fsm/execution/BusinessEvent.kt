package org.suggs.fsm.execution

open class BusinessEvent(val type: String,
                    val identifier: BusinessObjectIdentifier){

    override fun toString(): String {
        return "BusinessEvent(type='$type', identifier=$identifier)"
    }
}
