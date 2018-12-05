package org.suggs.fsm.engine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suggs.fsm.event.IEventFactory;
import org.suggs.fsm.framework.spi.IFsmEventInterceptor;
import org.suggs.fsm.framework.spi.IStateManager;
import org.suggs.fsm.uml2.scribe.runtime.IStateMachineContext;

import java.util.Map;

public class StateMachineContext implements IStateMachineContext {

    private static final Logger LOG = LoggerFactory.getLogger(StateMachineContext.class);

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

    public Map<String, String> getHistoryStates() {
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
    @Override
    public String toString() {
        return "StateMachineContext{" +
                "fsmEventInterceptor_=" + fsmEventInterceptor_ +
                ", eventFactory_=" + eventFactory_ +
                ", stateManager_=" + stateManager_ +
                '}';
    }

}
