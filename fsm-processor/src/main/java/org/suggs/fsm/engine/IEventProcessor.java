package org.suggs.fsm.engine;

import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

/**
 * The internal FSM event processing interface. */
public interface IEventProcessor
{

    /**
     * Processes a specified event within specified event, namespace and state machine contexts. */
    void processEvent( IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext );

}
