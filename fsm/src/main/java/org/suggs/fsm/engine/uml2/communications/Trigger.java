package org.suggs.fsm.engine.uml2.communications;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.suggs.fsm.engine.uml2.kernel.NamedElement;
import org.suggs.fsm.uml2.communications.IEvent;
import org.suggs.fsm.uml2.communications.ITrigger;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;

import static org.suggs.fsm.common.StringStyle.DEFAULT_TO_STRING_STYLE;

public class Trigger extends NamedElement implements ITrigger {

    private IEvent event_ = new DefaultTriggerEvent();

    public IEvent getEvent() {
        return event_;
    }

    public void setEvent(IEvent event) {
        event_ = event;
    }

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        super.acceptConstraintVisitor(constraintVisitor);
        constraintVisitor.visitTrigger(this);
    }

    /**
     * Default event type for triggers. This is provided so that
     * completion (automatic) transitions can be defined by creating a
     * transition with an eventless trigger.
     */
    protected class DefaultTriggerEvent extends Event {

        public DefaultTriggerEvent() {
            super(IEvent.COMPLETION_EVENT_NAME, null);
        }

    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    public String toString() {
        return new ToStringBuilder(this, DEFAULT_TO_STRING_STYLE).appendSuper(super.toString())
                .append("event", event_)
                .toString();
    }
}
