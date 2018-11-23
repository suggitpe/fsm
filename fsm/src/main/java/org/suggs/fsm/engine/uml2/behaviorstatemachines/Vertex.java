package org.suggs.fsm.engine.uml2.behaviorstatemachines;

import org.suggs.fsm.engine.uml2.kernel.NamedElement;
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.behaviorstatemachines.IVertex;
import org.suggs.fsm.uml2.communications.ITrigger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class Vertex extends NamedElement implements IVertex {

    private IRegion container_;

    private Set incomingTransitions_ = new HashSet();

    private Set outgoingTransitions_ = new HashSet();

    private List ancestorList = null;

    protected List prioritisedOutgoingTransitions_;

    public void setContainer(IRegion container) {
        container_ = container;
    }

    public IRegion getContainer() {
        return container_;
    }

    public void setIncoming(Set incomingTransitions) {
        incomingTransitions_ = incomingTransitions;
    }

    public Set getIncoming() {
        return incomingTransitions_;
    }

    public void setOutgoing(Set outgoingTransitions) {
        outgoingTransitions_ = outgoingTransitions;
    }

    public Set getOutgoing() {
        return outgoingTransitions_;
    }

    public List getAncestorList() {

        if (ancestorList == null) {
            ancestorList = new ArrayList();
            ancestorList.add(getContainer());
            ancestorList.addAll(getContainer().getAncestorList());
        }

        return ancestorList;
    }

    public void addIncomingTransition(ITransition transition) {
        incomingTransitions_.add(transition);
    }

    public void addOutgoingTransition(ITransition transition) {
        outgoingTransitions_.add(transition);
    }

    public List getAllPossibleOutgoingTransitions() {

        List allPossibleTransitions = new ArrayList();

        allPossibleTransitions.add(0, outgoingTransitions_);

        if (null != getContainer().getState()) {

            List inheritedTransitions = getContainer().getState().getAllPossibleOutgoingTransitions();

            for (int i = 0; i < inheritedTransitions.size(); i++) {

                Set priorityLevelSet = new HashSet();

                Set s = (Set) inheritedTransitions.get(i);

                for (Iterator transitionIt = s.iterator(); transitionIt.hasNext(); ) {
                    ITransition transition = (ITransition) transitionIt.next();
                    ITransition extendedTransition = transition.createShallowCopy();
                    extendedTransition.setIncomingVertex(this);
                    priorityLevelSet.add(extendedTransition);

                }

                allPossibleTransitions.add(i + 1, priorityLevelSet);
            }
        }

        return allPossibleTransitions;
    }

    /**
     * Returns the potential outgoing transitions for the specified
     * event type. This method effectively filters the output from
     * {@link #getAllPossibleOutgoingTransitions()}using the event
     * type to restrict the outgoing transitions returned.
     * <p>
     * NOTE: The method does not evaluate the guards on the
     * transitions.
     *
     * @param eventType The event type to compare with the triggers on the
     *                  transitions.
     * @return The outgoing transitions that could be triggered by the
     * specified event type.
     */
    protected List getAllPossibleOutgoingTransitions(String eventType) {

        List allTransitions = getAllPossibleOutgoingTransitions();

        List triggerableTransitions = new ArrayList();

        for (Object allTransition : allTransitions) {
            Set priorityLevelSet = (Set) allTransition;

            Set triggerableSet = new HashSet();

            for (Object aPriorityLevelSet : priorityLevelSet) {
                ITransition transition = (ITransition) aPriorityLevelSet;

                List triggers = transition.getTriggers();

                for (Object trigger1 : triggers) {
                    ITrigger trigger = (ITrigger) trigger1;
                    if (trigger.getEvent().getQualifiedName().equals(eventType)) {

                        triggerableSet.add(transition);
                    }

                }
            }

            triggerableTransitions.add(triggerableSet);

        }

        return triggerableTransitions;
    }

}
