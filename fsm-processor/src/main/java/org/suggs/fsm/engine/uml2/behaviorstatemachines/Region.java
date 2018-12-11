package org.suggs.fsm.engine.uml2.behaviorstatemachines;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.engine.uml2.kernel.Namespace;
import org.suggs.fsm.uml2.behaviorstatemachines.*;
import org.suggs.fsm.uml2.kernel.INamespace;
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor;
import org.suggs.fsm.uml2.scribe.namespacemgt.INamespaceObjectManager;
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser;
import org.suggs.fsm.uml2.scribe.runtime.IStateEntryListener;

import java.util.*;

/**
 * mapped
 */
public class Region extends Namespace implements IRegion {

    private static final Logger LOG = LoggerFactory.getLogger(Region.class);

    private IStateMachine stateMachine;
    private IState state;
    private ITransition[] transitions = new ITransition[0];
    private IVertex[] subVertices = new IVertex[0];
    private INamespace owner;

    public IState getState() {
        return state;
    }

    public IStateMachine getStateMachine() {
        return stateMachine;
    }

    public Set<ITransition> getTransitions() {
        Set<ITransition> transitions = new HashSet<>();
        Collections.addAll(transitions, this.transitions);
        return transitions;
    }

    public Set<IVertex> getSubVertices() {
        Set<IVertex> vertices = new HashSet<>();
        Collections.addAll(vertices, subVertices);
        return vertices;
    }

    public void setState(IState state) {
        if (null == owner) {
            this.state = state;
            // Set the owner
            owner = state;
        } else {
            LOG.error("The owner of region " + this + " is already set to " + owner + ": cannot set to " + state);
            throw new RuntimeException("The owner of region " + this + " is already set to " + owner + ": cannot set to " + state);
        }
    }

    public void setStateMachine(IStateMachine stateMachine) {
        this.stateMachine = stateMachine;
        // Set the owner
        owner = stateMachine;
    }

    public void setTransitions(Set<ITransition> transitions) {
        this.transitions = transitions.toArray(new ITransition[transitions.size()]);

        // Add the subVertices to the region namespace
        for (ITransition iTransition : this.transitions) {
            addOwnedMember(iTransition);
            // Set the container to this region
            iTransition.setContainer(this);
        }
    }

    public void setSubVertices(Set<IVertex> subVertices) {
        for (IVertex vertex : subVertices) {
            addSubVertex(vertex);
        }
    }

    public IPseudoState getInitialState() {
        for (IVertex iVertex : subVertices) {
            if (iVertex instanceof PseudoState && ((PseudoState) iVertex).isInitialPseudostate()) {
                return (IPseudoState) iVertex;
            }
        }
        String msg = "No initial pseudostate found for region " + this;
        LOG.error(msg);
        throw new RuntimeException(msg);
    }

    public void acceptNamespaceObjectManager(INamespaceObjectManager namespaceObjectManager) {
        super.acceptNamespaceObjectManager(namespaceObjectManager);
        namespaceObjectManager.visitRegion(this);
    }

    public void acceptConstraintVisitor(IConstraintVisitor constraintVisitor) {
        super.acceptConstraintVisitor(constraintVisitor);
        constraintVisitor.visitRegion(this);
    }

    public List<IRegion> getAncestorList() {
        if (null != state) {
            // This region is owned by a state
            return state.getAncestorList();
        } else if (null != stateMachine) {
            /*
             * This region is owned directly by a state machine. Do not recurse up as state machines define a
             * namespace boundary. The impleication of this is that transitions cannot link states from two state machines.
             */
            return new ArrayList<>();
        } else {
            String msg = " Region " + this + " is not owned by a state or state machine ";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }
    }

    public void acceptOptimiser(IModelOptimiser modelOptimiser) {
        // Optimise the transitions owned by the region
        Set<ITransition> transitions = getTransitions();
        for (ITransition transition : transitions) {
            transition.acceptOptimiser(modelOptimiser);
        }

        // Optimise the sub-vertices owned by the region
        Set<IVertex> subVertices = getSubVertices();
        for (IVertex vertex : subVertices) {
            vertex.acceptOptimiser(modelOptimiser);
        }
    }

    public void addStateEntryListener(boolean recurse, IStateEntryListener stateEntryListener) {
        // If deepHistory, recurse into regions in compound states
        Set<IVertex> subVertices = getSubVertices();
        for (IVertex vertex : subVertices) {
            if (vertex instanceof IState) {
                ((IState) vertex).addStateEntryListener(recurse, stateEntryListener);
            }
        }
    }

    public void addSubVertex(IVertex vertex) {
        // Test for existing matching vertex
        for (IVertex iVertex : subVertices) {
            if (iVertex.equals(vertex)) {
                return;
            }
        }

        IVertex[] newArray = new IVertex[1 + subVertices.length];
        System.arraycopy(subVertices, 0, newArray, 0, subVertices.length);
        newArray[newArray.length - 1] = vertex;

        subVertices = newArray;

        // Add the sub-vertices to the region namespace
        addOwnedMember(vertex);

        // Set the container on the sub-vertices to this region
        vertex.setContainer(this);

    }

}
