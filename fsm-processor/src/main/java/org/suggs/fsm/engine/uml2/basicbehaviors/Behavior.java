package org.suggs.fsm.engine.uml2.basicbehaviors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.engine.uml2.kernel.Namespace;
import org.suggs.fsm.framework.spi.IActionExecutor;
import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

public class Behavior extends Namespace implements IBehavior {

    private static final Logger LOG = LoggerFactory.getLogger(Behavior.class);

    private IBehavioredClassifier context;
    private IActionExecutor actionExecutor;

    public IBehavioredClassifier getContext() {
        return context;
    }

    public void setContext(IBehavioredClassifier context) {
        this.context = context;
    }

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        constraintVisitor.visitBehavior(this);
    }

    public void acceptOptimiser(IModelOptimiser modelOptimiser) {
        modelOptimiser.optimiseBehavior(this);
    }

    public void execute(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachineContext) {
        // Get the action executor from the namespace
        IActionExecutor action;
        try {
            action = (IActionExecutor) namespaceContext.getNamespaceObjectManager().getObject(getActionExecutor().getName());
        } catch (Throwable t) {
            LOG.error("Error getting action executor for " + this.toString());
            throw new RuntimeException(t);
        }

        // Call the action
        try {
            action.executeAction(eventContext);
        } catch (Throwable t) {
            LOG.error("Action executor " + this.toString() + " threw " + t.toString() + " for eventContext=" + eventContext);
            throw new RuntimeException(t);
        }

        // Notify listeners
        try {
            stateMachineContext.getEventInterceptor().onActionExecuted(this, eventContext, namespaceContext, stateMachineContext);
        } catch (Throwable t) {
            LOG.error("Error notifying event interceptor " + stateMachineContext.getEventInterceptor() + " for for eventContext="
                    + eventContext + ", namespaceContext=" + namespaceContext + ", stateMachineContext=" + stateMachineContext);
            throw new RuntimeException(t);
        }
    }

    public IActionExecutor getActionExecutor() {
        return actionExecutor;
    }

    public void setActionExecutor(IActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    public void acceptNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        super.acceptNamespaceObjectManager(namespaceObjectManager);

        // Add the action
        if (null != actionExecutor) {
            namespaceObjectManager.addObject(actionExecutor.getName(), actionExecutor);
        }
    }
}
