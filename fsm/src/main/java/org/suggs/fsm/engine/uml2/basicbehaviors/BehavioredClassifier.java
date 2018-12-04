package org.suggs.fsm.engine.uml2.basicbehaviors;

import org.suggs.fsm.engine.uml2.kernel.Namespace;
import org.suggs.fsm.uml2.basicbehaviors.IBehavior;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.scribe.constraints.IConstrainedObject;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;

public class BehavioredClassifier extends Namespace implements IBehavioredClassifier, IConstrainedObject {

    private IBehavior ownedBehavior_;

    public IBehavior getOwnedBehavior() {
        return ownedBehavior_;
    }

    public void setOwnedBehavior(IBehavior ownedBehavior) {
        ownedBehavior_ = ownedBehavior;
        ownedBehavior_.setContext(this);
    }

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        constraintVisitor.visitBehavioredClassifier(this);
    }

    public void acceptOptimiser(IModelOptimiser modelOptimiser) {
        getOwnedBehavior().acceptOptimiser(modelOptimiser);
    }

    public void acceptNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        super.acceptNamespaceObjectManager(namespaceObjectManager);
        // Visit the owned behavior
        ownedBehavior_.acceptNamespaceObjectManager(namespaceObjectManager);
    }
}
