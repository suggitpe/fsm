package org.suggs.fsm.uml2.behaviorstatemachines;

import org.suggs.fsm.uml2.scribe.runtime.IStateEntryListener;

/**
 * Defines a specialisation of {@link IPseudoState}that can be
 * notified when other states are entered in order to provide UML
 * history state functionality.
 */
public interface IHistoryState extends IPseudoState, IStateEntryListener {
}
