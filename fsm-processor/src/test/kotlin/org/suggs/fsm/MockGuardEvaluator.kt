package org.suggs.fsm

import org.slf4j.LoggerFactory
import org.suggs.fsm.framework.spi.IGuardEvaluator
import org.suggs.fsm.uml2.scribe.runtime.IEventContext

class MockGuardEvaluator(private var guardId: String = "",
                         private var returnValue: Boolean = true) : IGuardEvaluator {

    constructor(guardId: String) : this(guardId, true)

    private val log = LoggerFactory.getLogger(MockGuardEvaluator::class.java)

    override fun evaluateGuard(eventContext: IEventContext): Boolean {
        log.debug("Evaluating $guardId, returning [$returnValue]")
        return returnValue
    }

    override fun getName(): String? {
        return guardId
    }

    override fun setName(name: String) {
        guardId = name
    }
}