package org.suggs.fsm.engine.uml2.constraints.kernel;

import org.suggs.fsm.common.Assert;
import org.suggs.fsm.uml2.kernel.IConstraint;

public class ConstraintChecker extends NamedElementChecker implements IConstraintChecker {

    public void checkConstraint(IConstraint constraint) {

        // Guard evaluators cannot be null
        Assert.state(null != constraint.getGuardEvaluator(), "Null guard evaluator");

    }
}
