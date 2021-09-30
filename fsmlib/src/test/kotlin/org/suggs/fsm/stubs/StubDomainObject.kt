package org.suggs.fsm.stubs

data class StubDomainObject(var complete: Boolean = false) {

    fun areYouComplete(): Boolean = complete
}