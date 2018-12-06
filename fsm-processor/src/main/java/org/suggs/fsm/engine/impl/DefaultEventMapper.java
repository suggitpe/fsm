package org.suggs.fsm.engine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.event.IEvent;
import org.suggs.fsm.event.IEventMapper;
import org.suggs.fsm.uml2.kernel.INamespace;


public class DefaultEventMapper implements IEventMapper {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultEventMapper.class);

    private String stateMachineName;

    /**
     * Constructs a new instance with a default name of "context" as the name
     * of the state machine behaviored classifier.
     */
    public DefaultEventMapper() {
        this("context");
    }

    /**
     * Constructs a new instance with a using the passed name as the name
     * of the state machine behaviored classifier.
     */
    public DefaultEventMapper(String stateMachineName) {
        setStateMachineName(stateMachineName);
    }

    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }

    public String getStateMachineName() {
        return stateMachineName;
    }

    /**
     * Maps the specified event to a new event (the return value may
     * be the same object, or null if the mapping could not complete). Checks
     * to see if the name of the event starts with INamespace.TOP_LEVEL_NAMESPACE
     * + INamespace.NAMESPACE_SEPARATOR + getStateMachineName() + INamespace.NAMESPACE_SEPARATOR,
     * and if not, prepends this to the name of the passed event.
     */
    public IEvent map(IEvent event) {
        String eventHeader = INamespace.TOP_LEVEL_NAMESPACE +
                INamespace.NAMESPACE_SEPARATOR +
                stateMachineName +
                INamespace.NAMESPACE_SEPARATOR;

        if (event != null && event.getType() != null) {
            LOG.debug("Before: " + event.getType());

            if (!event.getType().startsWith(eventHeader)) {
                LOG.debug("Mapping Event");
                event.setType(eventHeader + event.getType());
            }

            LOG.debug("After: " + event.getType());
        }
        return event;
    }

}
