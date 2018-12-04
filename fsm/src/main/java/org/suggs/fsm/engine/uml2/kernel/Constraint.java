/*
 * Constraint.java created on 10-Nov-2005 12:34:51 by billinro for project FsmLib
 *
 * Copyright (c) 2005 UBS Investment Bank
 */
package org.suggs.fsm.engine.uml2.kernel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.framework.spi.IGuardEvaluator;
import org.suggs.fsm.uml2.kernel.IConstraint;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

public class Constraint extends NamedElement implements IConstraint {

    private static final Logger LOG = LoggerFactory.getLogger(Constraint.class);

    private IGuardEvaluator guardEvaluator_ = null;

    public IGuardEvaluator getGuardEvaluator() {
        return guardEvaluator_;
    }

    public void setGuardEvaluator(IGuardEvaluator guardEvaluator) {
        guardEvaluator_ = guardEvaluator;
    }

    public boolean evaluate(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {

        boolean result = false;

        // Get the guard evaluator from the namespace
        IGuardEvaluator evaluator = null;
        try {
            evaluator = (IGuardEvaluator) namespaceContext.getNamespaceObjectManager().getObject(getGuardEvaluator().getName());
        } catch (Throwable t) {
            LOG.error("Error getting guard evaluator for " + this.toString());
            throw new RuntimeException(t);
        }

        // Evaluate the guard
        try {
            result = evaluator.evaluateGuard(eventContext);
        } catch (Throwable t) {
            LOG.error("Guard evaluator " + this.toString() + " threw " + t.toString() + " for eventContext=" + eventContext);

            throw new RuntimeException(t);

        }

        // Notify listeners
        try {
            stateMachineContext.getEventInterceptor().onGuardEvaluated(this, result, eventContext, namespaceContext, stateMachineContext);
        } catch (Throwable t) {
            LOG.error("Error notifying event interceptor " + stateMachineContext.getEventInterceptor() + " for for eventContext="
                    + eventContext + ", namespaceContext=" + namespaceContext + ", stateMachineContext=" + stateMachineContext);
            throw new RuntimeException(t);
        }

        return result;
    }

    public void acceptNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager) {

        super.acceptNamespaceObjectManager(namespaceObjectManager);

        // Add the guard evaluator
        if (null != guardEvaluator_) {
            namespaceObjectManager.addObject(guardEvaluator_.getName(), guardEvaluator_);
        }
    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    @Override
    public String toString() {
        return "Constraint{" +
                "guardEvaluator_=" + guardEvaluator_ +
                '}';
    }
}
