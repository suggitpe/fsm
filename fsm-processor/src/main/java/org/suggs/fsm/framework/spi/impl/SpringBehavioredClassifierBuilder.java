package org.suggs.fsm.framework.spi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.suggs.fsm.framework.spi.IBehavioredClassifierBuilder;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;

import static org.suggs.fsm.common.Assert.checkNotNull;

/**
 * Spring-based implementation of IBehavioredClassifierBuilder.
 * <p>
 * This implementation builds an object graph for a state machine
 * based on a Spring beans configuration file.
 */
public class SpringBehavioredClassifierBuilder implements IBehavioredClassifierBuilder {

    /**
     * The file extension added to the state model ID to create the
     * Spring beans configuration filename
     */
    private static final String NAMESPACE_DEFINITION_EXTENSION = ".xml";

    private static final Logger LOG = LoggerFactory.getLogger(SpringBehavioredClassifierBuilder.class);

    /**
     * Builds a state machine object graph based on a Spring beans
     * configuration file. The configuration filename is constructed
     * by appending .xml to the stateModelId provided.
     */
    public IBehavioredClassifier createBehavioredClassifier(String stateModelId) {

        checkNotNull(stateModelId, "stateModelId can not be null");

        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(stateModelId + NAMESPACE_DEFINITION_EXTENSION);
            return (IBehavioredClassifier) context.getBean("context");
        } catch (Throwable t) {
            String msg = "Error loading Spring bean definition for stateModelId: " + stateModelId + " because of " + t.getMessage();
            LOG.error(msg, t);
            throw new RuntimeException(msg, t);
        }
    }
}
