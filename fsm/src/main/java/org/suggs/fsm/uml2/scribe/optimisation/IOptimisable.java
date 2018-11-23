package org.suggs.fsm.uml2.scribe.optimisation;

/**
 * Objects that implement this interface may perform pre-runtime
 * optimisation of their internal structure and that of their
 * dependent objects.
 */
public interface IOptimisable {

    /**
     * Optimises the object for runtime. The exact optimisation steps
     * depend on the type of object.
     */
    void acceptOptimiser(IModelOptimiser modelOptimiser);

}
