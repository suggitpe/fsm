package org.suggs.fsm.uml2.behaviorstatemachines;

import org.suggs.fsm.engine.IEventProcessor;
import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.communications.ITrigger;
import org.suggs.fsm.uml2.kernel.INamespace;
import org.suggs.fsm.uml2.scribe.runtime.IEnterable;
import org.suggs.fsm.uml2.scribe.runtime.IEntryReporter;
import org.suggs.fsm.uml2.scribe.runtime.IExitable;

import java.util.Set;

/**
 * Defines an implementation of the UML 2.0 State class. States model
 * a situation in which an invariant condition holds.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface IState extends INamespace, IVertex, IEnterable, IExitable, IEntryReporter, IEventProcessor {

    /**
     * The State kind for a Simple State
     */
    String SIMPLE = "SIMPLE";

    /**
     * The State kind for a Simple Composite State
     */
    String SIMPLE_COMPOSITE = "SIMPLE_COMPOSITE";

    /**
     * The State kind for a transient state. Note that this is not part of
     * the UML 2 Superstructure, but our own extension.
     */
    String TRANSIENT = "TRANSIENT";

    /**
     * A marker 'state' name used to indicate that the state machine
     * has exited one state but has not yet entered the next. This
     * value should never be the final state of the state machine if
     * an event has been handled successfully.
     */
    String TRANSITIONING = "TRANSITIONING";

    /**
     * Sets the entry behavior for this state.
     */
    void setEntryBehavior(IBehavior behavior);

    /**
     * @return The entry behavior for this state.
     */
    IBehavior getEntryBehavior();

    /**
     * Sets the exit behavior for this state.
     */
    void setExitBehavior(IBehavior behavior);

    /**
     * @return The exit behavior for this state.
     */
    IBehavior getExitBehavior();

    /**
     * Sets the region owned by this state.
     */
    void setRegion(IRegion region);

    /**
     * Returns the region owned by this state.
     *
     * @return The region owned by this state.
     */
    IRegion getRegion();

    /**
     * @return <code>true</code> if this state is of kind
     * IState.SIMPLE_COMPOSITE, <code>false</code> otherwise
     */
    boolean isComposite();

    /**
     * @return <code>true</code> if this state is of kind
     * IState.SIMPLE, <code>false</code> otherwise
     */
    boolean isSimple();


    /**
     * @return <code>true<./code> if this state is of kind
     * IState.TRANSIENT, <code>false</code> otherwise.
     */
    boolean isTransient();

    /**
     * @return <code>true</code> if this state is of kind
     * IState.SUBMACHINE, <code>false</code> otherwise
     */
    boolean isSubmachineState();

    /**
     * Gets the kind of the state, e.g. SIMPLE or SIMPLE_COMPOSITE.
     *
     * @return The kind of this state.
     */
    String getStateKind();

    /**
     * @return a set of <code>ITrigger</code> s for events that are
     * candidates to be retained by the state machine if they
     * trigger no transitions out of this state.
     */
    Set<ITrigger> getDeferrableTriggers();

    /**
     * Sets the set of triggers for the events which are deferrable by
     * this state.
     */
    void setDeferrableTriggers(Set<ITrigger> triggerList);

    /**
     * Returns true if the passed event is a deferrable event by this
     * state.
     */
    boolean defersEvent(IEvent event);

    /**
     * Returns the set of all deferrable event triggers for this
     * state. This includes the deferrable events registered on the
     * state itself, plus the ones registered on all its enclosing
     * states.
     */
    Set<ITrigger> getAllPossibleDeferrableTriggers();

}
