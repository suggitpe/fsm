package org.suggs.fsm.engine.uml2.behaviorstatemachines;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.engine.uml2.basicbehaviors.Behavior;
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState;
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;
import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine;
import org.suggs.fsm.uml2.behaviorstatemachines.IVertex;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;

import java.util.Iterator;
import java.util.List;

public class StateMachine extends Behavior implements IStateMachine {

    private static final Logger LOG = LoggerFactory.getLogger(StateMachine.class);

    private IRegion ownedRegion_ = null;

    public IRegion getOwnedRegion() {
        return ownedRegion_;
    }

    public void setOwnedRegion(IRegion ownedRegion) {
        ownedRegion_ = ownedRegion;

        // Associate the region with this state machine
        ownedRegion_.setStateMachine(this);

        // Add the owned region to this namespace
        addOwnedMember(ownedRegion_);
    }

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        constraintVisitor.visitStateMachine(this);
    }

    public void acceptNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        super.acceptNamespaceObjectManager(namespaceObjectManager);
        namespaceObjectManager.visitStateMachine(this);
    }

    public IPseudoState getInitialStableState() {
        return ownedRegion_.getInitialState();
    }

    public static IRegion getLeastCommonAncestor(IVertex s1, IVertex s2) {

        if (null == s1 || null == s2) {
            String msg = "getLeastCommonAncestor() called with null state(s), s1 = " + s1 + ", s2 = " + s2;
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        // Get the full ancestor trees for both states
        List<IRegion> s1AncestorList = s1.getAncestorList();
        List<IRegion> s2AncestorList = s2.getAncestorList();

        // Find the first match
        for (IRegion iRegion : s1AncestorList) {
            Region region = (Region) iRegion;
            if (s2AncestorList.contains(region)) {
                // Found the LCA
                return region;
            }
        }

        // No LCA found
        String msg = "No LCA Found for states s1 = " + s1 + ", s2 = " + s2;
        LOG.error(msg);
        throw new RuntimeException(msg);
    }

    public void acceptOptimiser(IModelOptimiser modelOptimiser) {
        ownedRegion_.acceptOptimiser(modelOptimiser);
    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    @Override
    public String toString() {
        return "StateMachine{" +
                "ownedRegion_=" + ownedRegion_.getState() +
                '}';
    }
}
