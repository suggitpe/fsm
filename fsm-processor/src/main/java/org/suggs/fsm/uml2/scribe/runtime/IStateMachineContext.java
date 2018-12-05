package org.suggs.fsm.uml2.scribe.runtime;

import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.framework.spi.IFsmEventInterceptor;
import org.suggs.fsm.framework.spi.IStateManager;

import java.util.Map;

/**
 * An object that implements this interface stores internal attributes
 * of a state machine.
 */
public interface IStateMachineContext {

    /**
     * Gets the event interceptor for this context.
     *
     * @return The Event interceptor configured for the state machine,
     * or <code>null</code> if none is set.
     */
    IFsmEventInterceptor getEventInterceptor();

    /**
     * Sets the Event interceptor. the event interceptor may be called
     * when significant events occur within the state machine.
     */
    void setFsmEventInterceptor(IFsmEventInterceptor fsmEventInterceptor);

    /**
     * Returns the current history state mappings. History states,
     * when entered, will transition automatically to the last state
     * from which they received a state entry notification. A mapping
     * is maintained of fully-qualified history state name to the
     * fully-qualified name of the state to which they point.
     *
     * @return The map of history state name <code>String</code> s
     * to the fully qualified name <code>String</code> they
     * currently map to.
     */
    Map getHistoryStates();

    /**
     * Sets the state management persistence copmponent to be used by
     * the state machine.
     */
    void setStateManager(IStateManager stateManager);

    /**
     * Gets the state persistence component to be used by the state
     * machine for storing its current state.
     */
    IStateManager getStateManager();

    /**
     * Sets te event factory to be made availalble to elements of the
     * state machine for creating new events. The most likely use case
     * for creating a new event is creating completion events. Must
     * not be <code>null</code>.
     */
    void setEventFactory(IEventFactory eventFactory);

    /**
     * Gets the event factory set in this context.
     */
    IEventFactory getEventFactory();

}
