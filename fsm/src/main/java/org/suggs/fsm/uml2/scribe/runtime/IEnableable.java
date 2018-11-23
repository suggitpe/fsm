package org.suggs.fsm.uml2.scribe.runtime;

/**
 * An object that implement this interface may be enabled based on an
 * event and associated context.
 */
public interface IEnableable {

    /**
     * Evaluates the enable condition based on the supplied context.
     *
     * @return <code>true</code> if the supplied context enables this object, <code>false</code> otherwise
     */
    boolean isEnabled(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachinecontext);

}