package org.suggs.fsm.framework.spi.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.suggs.fsm.framework.spi.IBehavioredClassifierBuilder;
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier;

/**
 * Spring-based implementation of IBehavioredClassifierBuilder.
 * <p>
 * This implementation builds an object graph for a state machine
 * based on a Sping beans configuration file.
 */
public class SpringBehavioredClassifierBuilder implements IBehavioredClassifierBuilder {

    /**
     * The file extension added to the state model ID to create the
     * Spring beans configuration filename
     */
    private static final String NAMESPACE_DEFINITION_EXTENSION = ".xml";

    private static final Log LOG = LogFactory.getLog(SpringBehavioredClassifierBuilder.class);

    /**
     * Builds a state machine object graph based on a Spring beans
     * configuration file. The configuration filename is constructed
     * by appending .xml to the stateModelId provided.
     */
    public IBehavioredClassifier createBehavioredClassifier(String stateModelId) {

        Assert.notNull(stateModelId, "stateModelId can not be null");

        try {
            ClassPathResource res = new ClassPathResource(stateModelId + NAMESPACE_DEFINITION_EXTENSION, ClassUtils.getClassLoader(getClass()));

            BeanFactory bf = new XmlBeanFactory(res);

            IBehavioredClassifier behavioredClassifier = (IBehavioredClassifier) bf.getBean("context");

            return behavioredClassifier;
        } catch (Throwable t) {
            String msg = "Error loading Spring bean definition for stateModelId: " + stateModelId + " because of " + t.getMessage();
            LOG.error(msg, t);
            throw new GeneralRuntimeException(msg, t);
        }
    }
}
