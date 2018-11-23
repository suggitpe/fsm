package org.suggs.fsm.framework.spi.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.suggs.fsm.framework.spi.IGuardEvaluator;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

/**
 * A default guard evaluator that can be used as a placeholder when
 * guards may need to be replaced at runtime. If called, the evaluator
 * always throws an exception.
 */
public class GuardEvaluatorProxy implements IGuardEvaluator {

    private static final Log LOG = LogFactory.getLog(GuardEvaluatorProxy.class);

    private String name_;

    public boolean evaluateGuard(IEventContext eventContext) {

        String msg = "Guard evaluator " + name_ + " has not been mapped to an implementation";
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
