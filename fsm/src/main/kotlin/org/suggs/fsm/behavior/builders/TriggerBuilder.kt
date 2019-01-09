package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.Trigger


class TriggerBuilder(val name: String)
    : Builder<Trigger> {

    companion object {

        const val COMPLETION_EVENT_NAME = "\$COMPLETION"

        fun aTriggerCalled(name: String): TriggerBuilder {
            return TriggerBuilder(name)
        }
    }

    var eventBuilder: EventBuilder = aDefaultEvent()

    fun firedWith(event: EventBuilder): TriggerBuilder {
        eventBuilder = event
        return this
    }

    fun aDefaultEvent(): EventBuilder {
        return EventBuilder(COMPLETION_EVENT_NAME)
    }

    override fun build(): Trigger {
        return Trigger(name, eventBuilder.build())
    }

}
