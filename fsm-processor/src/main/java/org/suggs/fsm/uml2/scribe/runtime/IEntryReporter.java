package org.suggs.fsm.uml2.scribe.runtime;

/**
 * An object that implements this interface maintains an internal list
 * of listeners that it will notify when it is entered. The main
 * example of this is where a state <code>S</code> is contained
 * within a region that is monitored by a history state <code>H</code>.
 * When <code>S</code> is entered, it notifies <code>H</code>,
 * which may then update its internal reference to re-direct incoming
 * transitionBuilders to <code>S</code>.
 */
public interface IEntryReporter
{

    /**
     * Registers a listener with the reporter that will be notified
     * when the state is entered.
     * 
     * @param recurse
     *            Set to <code>true</code> if sub-states of this
     *            state should also be registered with the listener
     *            (i.e. for deep history).
     * @param listener
     *            The listener to register.
     */
    public void addStateEntryListener( boolean recurse, IStateEntryListener listener );

}