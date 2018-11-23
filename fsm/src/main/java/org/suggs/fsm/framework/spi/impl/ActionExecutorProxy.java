package org.suggs.fsm.framework.spi.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.suggs.fsm.framework.spi.IActionExecutor;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

/**
 * A default action executor that can be used as a placeholder when
 * actions may need to be replaced at runtime. If called, the executor
 * always throws an exception.
 */

public class ActionExecutorProxy implements IActionExecutor {

    private static final Log LOG = LogFactory.getLog(ActionExecutorProxy.class);

    private String name_;

    public void executeAction(IEventContext eventContext) {

        String msg = "ActionExecutorProxy " + name_ + " has not been mapped to an implementation";
        LOG.error(msg);
        throw new GeneralRuntimeException(msg);
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
    public String toString() {
        return new ToStringBuilder(this, StringStyle.DEFAULT_TO_STRING_STYLE).append("name", name_).toString();
    }
}
