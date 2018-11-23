package org.suggs.fsm.uml2.basicbehaviors;

import org.suggs.fsm.framework.spi.IActionExecutor;
import org.suggs.fsm.uml2.kernel.IClassifier;
import org.suggs.fsm.uml2.scribe.constraints.IConstrainedObject;
import org.suggs.fsm.uml2.scribe.optimisation.IOptimisable;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

/**
 * Defines an implementation of the UML 2.0 Behavior class. Behaviors
 * are used in Scribe to provide hooks to action implementations.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface IBehavior extends IClassifier, IConstrainedObject, IOptimisable {

    /**
     * Sets the context that ownws this behavior.
     */
    void setContext(IBehavioredClassifier context);

    /**
     * Gets the context that owns this behavior.
     */
    IBehavioredClassifier getContext();

    /**
     * Sets the action executor that implements the functional
     * behavior of this object.
     */
    void setActionExecutor(IActionExecutor actionExecutor);

    /**
     * Gets the action executor that this object delegates to.
     */
    IActionExecutor getActionExecutor();

    /**
     * Executes this object's behavior for the specified context.
     * Execution is delegated to the action executor set for this
     * object. Any <code>Throwable</code> thrown by the action
     * executor will be wrapped with a GeneralRuntimeException and
     * will be accessible using GeneralRuntimeException.getCause().
     */
    void execute(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext);

}
