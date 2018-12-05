package org.suggs.fsm.engine.uml2.namespacemgt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.behaviorstatemachines.*;
import org.suggs.fsm.uml2.communications.ITrigger;
import org.suggs.fsm.uml2.kernel.IConstraint;
import org.suggs.fsm.uml2.kernel.INamedElement;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;

import java.util.*;

public class NamespaceObjectManager implements INamespaceObjectManager {

    private static final Logger LOG = LoggerFactory.getLogger(NamespaceObjectManager.class);

    private Map objectMap_ = new HashMap();

    public void addObject(String qualifiedName, Object object) {

        if (null == qualifiedName) {
            String msg = "null qualified name specified for object " + object;
            LOG.error(msg);
            throw new RuntimeException(msg);
        } else {
            objectMap_.put(qualifiedName, object);
        }
    }

    public Object getObject(String qualifiedName) {

        if (null == objectMap_.get(qualifiedName)) {
            String msg = "No object bound to namespace name: " + qualifiedName;
            LOG.error(msg);
            throw new RuntimeException(msg);
        } else {
            return objectMap_.get(qualifiedName);
        }

    }

    public void visitNamedElement(INamedElement namedElement) {

        /*
         * This is the default behaviour for a named element. It
         * simply adds the object to the namespace object map without
         * recursing into contained obejcts.
         */
        addObject(namedElement.getQualifiedName(), namedElement);

    }

    public void visitRegion(IRegion region) {

        // Visit the contained transitions
        Set transitions = region.getTransitions();
        for (Iterator iter = transitions.iterator(); iter.hasNext(); ) {
            ITransition transition = (ITransition) iter.next();
            transition.acceptNamespaceObjectManager(this);
        }

        // Visit the contained sub-vertices
        Set vertices = region.getSubVertices();
        for (Iterator iter = vertices.iterator(); iter.hasNext(); ) {
            IVertex vertex = (IVertex) iter.next();
            vertex.acceptNamespaceObjectManager(this);
        }

    }

    public void visitState(IState state) {

        // Visit the region in the state
        if (null != state.getRegion()) {
            state.getRegion().acceptNamespaceObjectManager(this);
        }

        // Visit the entry and exit behaviors
        if (null != state.getEntryBehavior()) {
            state.getEntryBehavior().acceptNamespaceObjectManager(this);
        }
        if (null != state.getExitBehavior()) {
            state.getExitBehavior().acceptNamespaceObjectManager(this);
        }

        // Visit the triggers for deferred events
        Set triggers = state.getDeferrableTriggers();
        if (triggers != null) {
            for (Iterator iter = triggers.iterator(); iter.hasNext(); ) {
                ITrigger trigger = (ITrigger) iter.next();
                trigger.acceptNamespaceObjectManager(this);
            }
        }
    }

    public void visitStateMachine(IStateMachine stateMachine) {

        // Visit the owned region in the state machine
        stateMachine.getOwnedRegion().acceptNamespaceObjectManager(this);

    }

    public void visitBehavioredClassifier(IBehavioredClassifier behavioredClassifier) {

        behavioredClassifier.acceptNamespaceObjectManager(this);

    }

    public void visitTransition(ITransition transition) {

        // Visit the guard
        IConstraint guard = transition.getGuard();
        if (null != guard) {
            guard.acceptNamespaceObjectManager(this);
        }

        // Visit the transition action
        List effects = transition.getEffects();
        for (Iterator iter = effects.iterator(); iter.hasNext(); ) {
            IBehavior effect = (IBehavior) iter.next();
            effect.acceptNamespaceObjectManager(this);
        }

        // Visit the triggers
        List triggers = transition.getTriggers();
        for (Iterator iter = triggers.iterator(); iter.hasNext(); ) {
            ITrigger trigger = (ITrigger) iter.next();
            trigger.acceptNamespaceObjectManager(this);
        }

    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    @Override
    public String toString() {
        return "NamespaceObjectManager{" +
                "objectMap_=" + objectMap_ +
                '}';
    }
}
