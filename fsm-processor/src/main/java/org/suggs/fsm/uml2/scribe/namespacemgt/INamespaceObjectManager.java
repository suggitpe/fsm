package org.suggs.fsm.uml2.scribe.namespacemgt;

import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;
import org.suggs.fsm.uml2.behaviorstatemachines.IState;
import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine;
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.kernel.INamedElement;

/**
 * Implementations of this interface handles references to objects in
 * the namespace. The INamespaceObjectManager visits state machine
 * objects to build its internal map of qualified names to object
 * references.
 */
public interface INamespaceObjectManager {

    /**
     * Adds an object to the internal <code>Map</code> keyed on
     * qualifiedName. If an object with the same key already exists,
     * it will be replaced
     *
     * @param qualifiedName The unique name for the object. Must not be
     *                      <code>null</code>
     * @param object        The object reference to store.
     */
    void addObject(String qualifiedName, Object object);

    /**
     * Retrieves the object reference for a specified qualified name.
     *
     * @param qualifiedName The qualified name to search on
     * @return The object mapped to the name, or <code>null</code>
     * if not found.
     */
    Object getObject(String qualifiedName);

    /**
     * Visits an instance of a named element object and updates the
     * internal map of namespace objects by adding the qualified name
     * of the object as the key and the object as the value.
     *
     * @param namedObject The INamedElement to visit
     */
    void registerElement(INamedElement namedObject);

    /**
     * Visits an instance of a state machine object and updates the
     * internal map of namespace objects by adding the qualified name
     * of the object as the key and the object as the value.
     *
     * @param stateMachine The IStateMachine to visit
     */
    void visitStateMachine(IStateMachine stateMachine);

    /**
     * Visits an instance of a region object and updates the internal
     * map of namespace objects by adding the qualified name of the
     * object as the key and the object as the value.
     *
     * @param region The IRegion to visit
     */
    void visitRegion(IRegion region);

    /**
     * Visits an instance of a state object and updates the internal
     * map of namespace objects by adding the qualified name of the
     * object as the key and the object as the value.
     *
     * @param state The IState to visit
     */
    void visitState(IState state);

    /**
     * Visits an instance of a behaviored classifier object and
     * updates the internal map of namespace objects by adding the
     * qualified name of the object as the key and the object as the
     * value.
     *
     * @param behavioredClassifier The IBehavioredClassifier to visit
     */
    void visitBehavioredClassifier(IBehavioredClassifier behavioredClassifier);

    /**
     * Visits an instance of a transition object and updates the
     * internal map of namespace objects by adding the qualified name
     * of the object as the key and the object as the value.
     *
     * @param transition The ITransition to visit
     */
    void visitTransition(ITransition transition);
}
