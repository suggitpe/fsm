package org.suggs.fsm.framework.api;

import org.suggs.fsm.engine.IFsmEventInterceptorManager;
import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.framework.spi.IStateManager;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;

import java.util.Map;

/**
 * The main public interface for a state machine constructed using the
 * FSM library.
 */
public interface IStateMachine extends IFsmEventInterceptorManager {

    /**
     * Gets the namespace object manager used by the state machine.
     * The namespace object manager provide naming services internally
     * to the state machine, mapping fully qualified names from the
     * state model the objects that implement them. Access is provided
     * in this interface so that clients of the FSM library have the
     * option to replace guard or action implementations after the
     * state machine has been constructed as part of a <i>binding </i>
     * process.
     */
    INamespaceObjectManager getNamespaceObjectManager();

    /**
     * Processes the specified event and context on the caller's
     * thread. When this method is called, the state machine first
     * loads its current state from the specified IStateManager
     * implementation. If no state is returned, then the state machine
     * is initialised to its first stable state.
     * <p>
     * All errors during processing are considered to be unrecoverable
     * by the state machine. The state machine itself will not attempt
     * to roll back any actions or guards or calls to the state
     * manager made whilst processing the event. This functionality
     * must be implemented by the caller to ensure that the system is
     * consistent should event processing fail to complete
     * successfully.
     */
    void handleEvent(IEvent event, IStateManager stateManager, Map<String, String> context);
}