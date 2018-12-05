package org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines;

import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;

/**
 * Objects that implement this interface check regions for constraint
 * violations.
 */
public interface IRegionChecker {

    /**
     * Checks the region.
     */
    void checkConstraints(IRegion region);

}
