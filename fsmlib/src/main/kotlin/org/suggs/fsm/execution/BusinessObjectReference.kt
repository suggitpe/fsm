package org.suggs.fsm.execution

data class BusinessObjectReference(val domain: String,
                                   val id: String,
                                   val version: Long)
