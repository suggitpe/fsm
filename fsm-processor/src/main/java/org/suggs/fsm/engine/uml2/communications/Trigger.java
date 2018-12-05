package org.suggs.fsm.engine.uml2.communications;

import org.suggs.fsm.engine.uml2.kernel.NamedElement;
import org.suggs.fsm.uml2.communications.IEvent;
import org.suggs.fsm.uml2.communications.ITrigger;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;

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
    @Override
    public String toString() {
        return "Trigger{" +
                "event_=" + event_ +
                '}';
    }
}
