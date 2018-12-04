package org.suggs.fsm.framework.spi;

import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

import java.util.List;
import java.util.Map;

/**
 * Defines the contract for a state persistence component. An object
 * implementing this interface will handle state persistence for a
 * single state machine.
 * <p>
 * It is anticipated that the state manager will most likely be backed
 * by a persistent transactional resource so that changes to the state
 * machine state can be committed or rolled back as part of an atomic
 * event-handling transaction that may include actions.
 */
public interface IStateManager {

    /**
     * Stores the qualified name of the current active state for a
     * state machine.
     */
    void storeActiveState(String activeState);

    /**
     * Retrieves the qualified name of the current active state for a
     * state machine.
     */
    String getActiveState();

    /**
     * Stores the state that each history state in the a state machine
     * currently references.
     */
    void storeHistoryStates(Map<String, String> historyStateMap);

    /**
     * Retrieves the hsitory state mappings.
     */
    Map<String, String> getHistoryStates();

    /**
     * Stores the list of deferred events for a state machine.
     */
    void storeDeferredEvents(List<IEventContext> eventList);

    /**
     * Retrieves the list of deferred events for a state machine.
     */
    List<IEventContext> getDeferredEvents();

}
