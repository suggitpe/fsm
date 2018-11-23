package org.suggs.fsm.framework.spi.impl;

import org.springframework.util.Assert;
import org.suggs.fsm.framework.spi.IBehavioredClassifierBuilder;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;

import java.util.Map;

/**
 * BehavioredClassifierBuilder implementation which contains contexts
 * for FSMs which are injected at runtime (using Spring, or
 * programmatic control).
 * <p>
 * This implementation holds an object graph for a state machine.
 */
public class CachingBehavioredClassifierBuilder implements IBehavioredClassifierBuilder {

    private Map behaviouredClassifiers_;

    /**
     * Returns a state machine object graph from its internal cache.
     */
    public IBehavioredClassifier createBehavioredClassifier(String stateModelId) {

        Assert.notNull(stateModelId, "stateModelId can not be null");

        IBehavioredClassifier ret = (IBehavioredClassifier) behaviouredClassifiers_.get(stateModelId);
        if (ret == null) {
            throw new GeneralRuntimeException("No FSM definition for stateModelId: " + stateModelId);
        }
        return ret;

    }

    /**
     * Returns the value of behaviouredClassifiers.
     */
    public Map getBehaviouredClassifiers() {
        return behaviouredClassifiers_;
    }

    /**
     * Sets the behaviouredClassifiers field to the specified value.
     */
    public void setBehaviouredClassifiers(Map behaviouredClassifiers) {
        behaviouredClassifiers_ = behaviouredClassifiers;
    }
}
