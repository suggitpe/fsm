package org.suggs.fsm.execution

data class BusinessObjectIdentifier(val domain: String,
                                    val id: String,
                                    val version: Long)
