package org.suggs.fsm

import org.slf4j.LoggerFactory
import org.suggs.fsm.framework.spi.IActionExecutor
import org.suggs.fsm.uml2.scribe.runtime.IEventContext

class MockActionExecutor(private var actionId: String) : IActionExecutor {

    private val log = LoggerFactory.getLogger(MockActionExecutor::class.java)

    override fun executeAction(eventContext: IEventContext) {
        log.debug("Executing $actionId")
    }

    override fun getName(): String {
        return actionId
    }

    override fun setName(name: String) {
        actionId = name
    }
}