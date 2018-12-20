package org.suggs.fsm.uml2.communications;

import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.kernel.INamespace;

/**
 * Defines an implementation of the UML 2.0 Event class. Events are
 * occurrences that may potentially trigger transitionBuilders.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface IEvent extends INamedElement {

    /**
     * Name used to identify completion events that can trigger
     * completion (automatic) transitionBuilders.
     */
    String COMPLETION_EVENT_NAME = "$COMPLETION";

    String COMPLETION_EVENT_QUALIFIED_NAME = INamespace.TOP_LEVEL_NAMESPACE + INamespace.NAMESPACE_SEPARATOR + COMPLETION_EVENT_NAME;

    /**
     * Name used to identify 'null' events, in particular the event
     * used to transition from an pseudoStateKind pseudostate to the first
     * stable state of a region.
     */
    String NULL_EVENT_NAME = "$NULL";

    String NULL_EVENT_QUALIFIED_NAME = INamespace.TOP_LEVEL_NAMESPACE + INamespace.NAMESPACE_SEPARATOR + NULL_EVENT_NAME;

}
