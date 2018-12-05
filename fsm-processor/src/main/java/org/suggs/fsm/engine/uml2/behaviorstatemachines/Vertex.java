package org.suggs.fsm.engine.uml2.behaviorstatemachines;

import org.suggs.fsm.engine.uml2.kernel.NamedElement;
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion;
import org.suggs.fsm.uml2.behaviorstatemachines.ITransition;
import org.suggs.fsm.uml2.behaviorstatemachines.IVertex;
import org.suggs.fsm.uml2.communications.ITrigger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Vertex extends NamedElement implements IVertex {

    private IRegion container_;
    private Set<ITransition> incomingTransitions_ = new HashSet<>();
    private Set<ITransition> outgoingTransitions_ = new HashSet<>();
    private List<IRegion> ancestorList = null;
    protected List<Set<ITransition>> prioritisedOutgoingTransitions_;

    public void setContainer(IRegion container) {
        container_ = container;
    }

    public IRegion getContainer() {
        return container_;
    }

    public void setIncoming(Set<ITransition> incomingTransitions) {
        incomingTransitions_ = incomingTransitions;
    }

    public Set<ITransition> getIncoming() {
        return incomingTransitions_;
    }

    public void setOutgoing(Set<ITransition> outgoingTransitions) {
        outgoingTransitions_ = outgoingTransitions;
    }

    public Set<ITransition> getOutgoing() {
        return outgoingTransitions_;
    }

    public List<IRegion> getAncestorList() {

        if (ancestorList == null) {
            ancestorList = new ArrayList<>();
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

    public List<Set<ITransition>> getAllPossibleOutgoingTransitions() {

        List<Set<ITransition>> allPossibleTransitions = new ArrayList<>();

        allPossibleTransitions.add(0, outgoingTransitions_);

        if (null != getContainer().getState()) {

            List<Set<ITransition>> inheritedTransitions = getContainer().getState().getAllPossibleOutgoingTransitions();

            for (int i = 0; i < inheritedTransitions.size(); i++) {

                Set<ITransition> priorityLevelSet = new HashSet<>();

                Set<ITransition> s = inheritedTransitions.get(i);

                for (ITransition transition : s) {
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
    protected List<Set<ITransition>> getAllPossibleOutgoingTransitions(String eventType) {

        List<Set<ITransition>> allTransitions = getAllPossibleOutgoingTransitions();

        List<Set<ITransition>> triggerableTransitions = new ArrayList<>();

        for (Set<ITransition> allTransition : allTransitions) {

            Set<ITransition> triggerableSet = new HashSet<>();
            for (ITransition transition : allTransition) {
                List<ITrigger> triggers = transition.getTriggers();
                for (ITrigger trigger : triggers) {
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
