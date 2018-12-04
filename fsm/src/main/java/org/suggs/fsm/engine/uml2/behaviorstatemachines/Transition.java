package org.suggs.fsm.engine.uml2.behaviorstatemachines;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.engine.uml2.communications.Trigger;
import org.suggs.fsm.engine.uml2.kernel.NamedElement;
import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.behaviorstatemachines.IVertex;
import org.suggs.fsm.uml2.kernel.IConstraint;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;
import org.suggs.fsm.uml2.scribe.runtime.INamespaceContext;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Transition extends NamedElement implements ITransition {

    private static final Logger LOG = LoggerFactory.getLogger(Transition.class);

    private List effects_ = new ArrayList();
    private IConstraint guard_;
    private IVertex incomingVertex_;
    private IVertex outgoingVertex_;
    private List triggers_ = new DefaultTriggerSet();
    private IRegion container_;
    private ITransitionBehaviour transitionBehaviour_;

    public Transition(String transitionKind) {
        super();
        setTransitionKind(transitionKind);
    }

    public void setEffects(List effects) {
        effects_ = effects;
    }

    public List getEffects() {
        return effects_;
    }

    public void setGuard(IConstraint guard) {
        guard_ = guard;
    }

    public IConstraint getGuard() {
        return guard_;
    }

    public void setIncomingVertex(IVertex incomingVertex) {
        incomingVertex_ = incomingVertex;
    }

    public IVertex getIncomingVertex() {
        return incomingVertex_;
    }

    public void setOutgoingVertex(IVertex outgoingVertex) {
        outgoingVertex_ = outgoingVertex;
    }

    public IVertex getOutgoingVertex() {
        return outgoingVertex_;
    }

    public void setTransitionKind(String transitionKind) {

        if (ITransition.EXTERNAL.equals(transitionKind)) {
            transitionBehaviour_ = new ExternalTransitionBehaviour();

        } else if (ITransition.INTERNAL.equals(transitionKind)) {
            transitionBehaviour_ = new InternalTransitionBehaviour();

        } else {
            String msg = "Unrecognised transition kind: " + transitionKind;
            LOG.error(msg);
            throw new RuntimeException(msg);
        }
    }

    public String getTransitionKind() {
        return transitionBehaviour_.getTransitionKind();
    }

    public void setTriggers(List triggers) {
        triggers_ = triggers;
    }

    public List getTriggers() {
        return triggers_;
    }

    public IRegion getContainer() {
        return container_;
    }

    public void setContainer(IRegion container) {
        container_ = container;
    }

    public void acceptNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        super.acceptNamespaceObjectManager(namespaceObjectManager);
        namespaceObjectManager.visitTransition(this);
    }

    public void fire(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachinecontext) {
        transitionBehaviour_.fire(this, eventContext, namespaceContext, stateMachinecontext);
    }

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        super.acceptConstraintVisitor(constraintVisitor);
        constraintVisitor.visitTransition(this);
    }

    public boolean isEnabled(IEventContext eventContext, INamespaceContext namespaceContext, IStateMachineContext stateMachinecontext) {
        if (null == guard_) {
            return true;
        } else {
            return guard_.evaluate(eventContext, namespaceContext, stateMachinecontext);
        }
    }

    public ITransition createShallowCopy() {
        return (ITransition) clone();
    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    @Override
    public String toString() {
        return "Transition{" +
                "effects_=" + effects_ +
                ", guard_=" + guard_ +
                ", incomingVertex_=" + incomingVertex_ +
                ", outgoingVertex_=" + outgoingVertex_ +
                ", triggers_=" + triggers_ +
                ", container_=" + container_ +
                ", transitionBehaviour_=" + transitionBehaviour_ +
                '}';
    }

    protected Object clone() {

        Transition clonedTransition = new Transition(getTransitionKind());

        clonedTransition.setContainer(getContainer());
        clonedTransition.setEffects(getEffects());
        clonedTransition.setGuard(getGuard());
        clonedTransition.setIncomingVertex(getIncomingVertex());
        clonedTransition.setName(getName());
        clonedTransition.setNamespace(getNamespace());
        clonedTransition.setOutgoingVertex(getOutgoingVertex());
        clonedTransition.setTriggers(getTriggers());

        return clonedTransition;
    }

    public void acceptOptimiser(IModelOptimiser modelOptimiser) {
        incomingVertex_.addOutgoingTransition(this);
        outgoingVertex_.addIncomingTransition(this);
    }

    /**
     * Encapsulates transition behaviour that depends on the kind of
     * the transition.
     */
    private interface ITransitionBehaviour {

        void fire(ITransition transition, IEventContext eventContext, INamespaceContext namespaceContext,
                  IStateMachineContext stateMachinecontext);

        String getTransitionKind();
    }

    /**
     * The behaviour of an internal transition.
     */
    private class InternalTransitionBehaviour implements ITransitionBehaviour {

        public void fire(ITransition transition, IEventContext eventContext, INamespaceContext namespaceContext,
                         IStateMachineContext stateMachinecontext) {

            IVertex sourceVertex = getIncomingVertex();
            IVertex targetVertex = getOutgoingVertex();

            sourceVertex.exit(eventContext, namespaceContext, stateMachinecontext);

            // Do transition actions
            List effects = getEffects();
            for (Object effect : effects) {
                IBehavior behavior = (IBehavior) effect;
                behavior.execute(eventContext, namespaceContext, stateMachinecontext);
            }

            targetVertex.enter(eventContext, namespaceContext, stateMachinecontext);

        }

        public String getTransitionKind() {
            return ITransition.INTERNAL;
        }
    }

    /**
     * The behaviour of an external transition
     */
    private class ExternalTransitionBehaviour implements ITransitionBehaviour {

        public void fire(ITransition transition, IEventContext eventContext, INamespaceContext namespaceContext,
                         IStateMachineContext stateMachinecontext) {

            IVertex sourceVertex = getIncomingVertex();
            IVertex targetVertex = getOutgoingVertex();

            IRegion leastCommonAncestor = StateMachine.getLeastCommonAncestor(sourceVertex, targetVertex);

            {
                /*
                 * Get the list of states to exit. This is the source
                 * state plus any other states up to but not including
                 * the state that owns the least common ancestor of
                 * the source and target vertices.
                 */

                List exitList = sourceVertex.getAncestorList();

                // Exit the source state
                sourceVertex.exit(eventContext, namespaceContext, stateMachinecontext);

                sourceVertex.doExitAction(eventContext, namespaceContext, stateMachinecontext);

                // Exit the states in sequence
                boolean leastCommonAncestorReached = false;

                for (Iterator iter = exitList.iterator(); !leastCommonAncestorReached && iter.hasNext(); ) {
                    IRegion region = (IRegion) iter.next();
                    if (region != leastCommonAncestor) {
                        region.getState().doExitAction(eventContext, namespaceContext, stateMachinecontext);
                    } else {
                        leastCommonAncestorReached = true;
                    }
                }
            }

            // Do transition actions
            List effects = getEffects();
            for (Object effect : effects) {
                IBehavior behavior = (IBehavior) effect;
                behavior.execute(eventContext, namespaceContext, stateMachinecontext);
            }

            {
                /*
                 * Get the list of states to enter. This is the target
                 * state preceded by any other states between the
                 * least common ancestor and the taget vertex.
                 */

                List entryList = targetVertex.getAncestorList();

                boolean leastCommonAncestorReached = false;

                for (int i = entryList.size() - 1; i >= 0; i--) {
                    if (leastCommonAncestorReached == true) {
                        ((IRegion) entryList.get(i)).getState().doEntryAction(eventContext, namespaceContext, stateMachinecontext);
                    } else if (entryList.get(i) == leastCommonAncestor) {
                        leastCommonAncestorReached = true;
                    }
                }

                // Enter the target state

                targetVertex.doEntryAction(eventContext, namespaceContext, stateMachinecontext);

                targetVertex.enter(eventContext, namespaceContext, stateMachinecontext);

            }
        }

        public String getTransitionKind() {
            return ITransition.EXTERNAL;
        }

    }

    /**
     * The default triggers for a new transition.
     */
    protected class DefaultTriggerSet extends Vector implements List {

        DefaultTriggerSet() {
            // Add a single default trigger
            this.add(new Trigger());
        }
    }

}
