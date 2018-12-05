package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState;

public interface IPseudoStateChecker {

    void checkConstraints(IPseudoState pseudostate);
}
