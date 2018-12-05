package org.suggs.fsm.framework.spi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.framework.spi.IActionExecutor;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

/**
 * A default action executor that can be used as a placeholder when
 * actions may need to be replaced at runtime. If called, the executor
 * always throws an exception.
 */

public class ActionExecutorProxy implements IActionExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(ActionExecutorProxy.class);

    private String name_;

    public void executeAction(IEventContext eventContext) {

        String msg = "ActionExecutorProxy " + name_ + " has not been mapped to an implementation";
        LOG.error(msg);
        throw new RuntimeException(msg);
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        name_ = name;
    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    @Override
    public String toString() {
        return "ActionExecutorProxy{" +
                "name_='" + name_ + '\'' +
                '}';
    }
}
