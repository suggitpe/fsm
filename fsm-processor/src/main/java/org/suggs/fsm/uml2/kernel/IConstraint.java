package org.suggs.fsm.uml2.kernel;

import org.suggs.fsm.framework.spi.IGuardEvaluator;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

/**
 * Defines an implementation of the UML 2.0 Constraint class.
 * Constraints are conditions used to encapsulate guard conditions.
 * <p>
 * Note that the interface may not define a complete representation of
 * the UML class - only those attributes relevant to Scribe state
 * machines are accessible and additional attributes may have been
 * added to support implementation of the state machine runtime.
 */
public interface IConstraint extends INamedElement {

    /**
     * Sets the guard evaluator to be used when the constraint is
     * evaluated.
     */
    void setGuardEvaluator(IGuardEvaluator guardEvaluator);

    /**
     * Gets the guard evaluator that will be used when this constraint
     * is evaluated.
     */
    IGuardEvaluator getGuardEvaluator();

    /**
     * Evaluates this constratint for the specified context.
     * Evaluation is delegated to the guard evaluator set for this
     * object. Any <code>Throwable</code> thrown by the guard
     * evaluator will be wrapped with a GeneralRuntimeException and
     * will be accessible using GeneralRuntimeException.getCause().
     */
    boolean evaluate(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext);
}
