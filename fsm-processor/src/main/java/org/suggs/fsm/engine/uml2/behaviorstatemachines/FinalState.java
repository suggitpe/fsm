package org.suggs.fsm.engine.uml2.behaviorstatemachines;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;
import org.suggs.fsm.uml2.communications.IEvent;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

import java.util.Set;

/**
 * Implementation of a UML Final State as per UML Modeling Language:
 * Superstructure Version 2.0 formal/05-07-04
 */
public class FinalState extends State implements IState {

    private static final Logger LOG = LoggerFactory.getLogger(FinalState.class);

    /**
     * Default constructor. The constructor sets the state kind to SIMPLE.
     */
    public FinalState() {
        super(IState.SIMPLE);
    }

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        constraintVisitor.visitFinalState(this);

    }

    public void enter(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {

        notifyStateEntryListeners(stateMachineContext);

        // Update persistent state.
        stateMachineContext.getStateManager().storeActiveState(getQualifiedName());

        /*
         * Send a completion event to the enclosing state (if there is
         * one) This may trigger a completion transition out of a
         * compound state.
         */
        if (null != getContainer().getState()) {
            eventContext.setEvent(stateMachineContext.getEventFactory().createEvent(IEvent.COMPLETION_EVENT_QUALIFIED_NAME,
                    eventContext.getEvent()));
            try {
                getContainer().getState().processEvent(eventContext, namespaceContext, stateMachineContext);
            } catch (UnprocessableEventException e) {
                LOG.debug("Completion event skipped: " + eventContext.getEvent());
                // OK to silently skip completion events
            }
        }

    }

    public Set getOutgoing() {
        return super.getOutgoing();
    }

    public void setOutgoing(Set outgoingTransitions) {
        super.setOutgoing(outgoingTransitions);
    }

}
