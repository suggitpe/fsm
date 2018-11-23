package org.suggs.fsm.engine.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.framework.spi.IFsmEventInterceptor;
import org.suggs.fsm.framework.spi.IStateManager;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

import java.util.Map;

import static org.suggs.fsm.common.StringStyle.DEFAULT_TO_STRING_STYLE;

public class StateMachineContext implements IStateMachineContext {

    private static final Log LOG = LogFactory.getLog(StateMachineContext.class);

    private IFsmEventInterceptor fsmEventInterceptor_ = null;

    private IEventFactory eventFactory_;

    /**
     * Provides state persistence services to this state machine.
     */
    private IStateManager stateManager_;

    public IFsmEventInterceptor getEventInterceptor() {
        return fsmEventInterceptor_;
    }

    public void setFsmEventInterceptor(IFsmEventInterceptor fsmEventInterceptor) {
        fsmEventInterceptor_ = fsmEventInterceptor;
    }

    public Map getHistoryStates() {
        return stateManager_.getHistoryStates();
    }

    public IStateManager getStateManager() {
        return stateManager_;
    }

    public void setStateManager(IStateManager stateManager) {
        stateManager_ = stateManager;
    }

    public IEventFactory getEventFactory() {
        return eventFactory_;
    }

    public void setEventFactory(IEventFactory eventFactory) {
        if (null == eventFactory) {
            String msg = "null event factory set on " + this.toString();
            LOG.error(msg);
            throw new RuntimeException(msg);
        }
        eventFactory_ = eventFactory;
    }

    /**
     * Returns a String representation of this object using the
     * default toString style.
     */
    public String toString() {
        return new ToStringBuilder(this, DEFAULT_TO_STRING_STYLE).append("fsmEventInterceptor", fsmEventInterceptor_)
                .append("stateManager", stateManager_)
                .toString();
    }

}
