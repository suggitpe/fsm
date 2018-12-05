package org.suggs.fsm.framework.spi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.framework.spi.IGuardEvaluator;
import org.suggs.fsm.uml2.scribe.runtime.IEventContext;

/**
 * A default guard evaluator that can be used as a placeholder when
 * guards may need to be replaced at runtime. If called, the evaluator
 * always throws an exception.
 */
public class GuardEvaluatorProxy implements IGuardEvaluator {

    private static final Logger LOG = LoggerFactory.getLogger(GuardEvaluatorProxy.class);

    private String name_;

    public boolean evaluateGuard(IEventContext eventContext) {

        String msg = "Guard evaluator " + name_ + " has not been mapped to an implementation";
        LOG.error(msg);
        throw new RuntimeException(msg);
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        name_ = name;
    }

    @Override
    public String toString() {
        return "GuardEvaluatorProxy{" +
                "name_='" + name_ + '\'' +
                '}';
    }
}
