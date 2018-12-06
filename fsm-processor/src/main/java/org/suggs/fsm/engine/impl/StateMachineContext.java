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

    private IFsmEventInterceptor fsmEventInterceptor = null;
    private IEventFactory eventFactory;
    private IStateManager stateManager;

    public IFsmEventInterceptor getEventInterceptor() {
        return fsmEventInterceptor;
    }

    public void setFsmEventInterceptor(IFsmEventInterceptor fsmEventInterceptor) {
        this.fsmEventInterceptor = fsmEventInterceptor;
    }

    public Map<String, String> getHistoryStates() {
        return stateManager.getHistoryStates();
    }

    public IStateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(IStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public IEventFactory getEventFactory() {
        return eventFactory;
    }

    public void setEventFactory(IEventFactory eventFactory) {
        if (null == eventFactory) {
            String msg = "null event factory set on " + this.toString();
            LOG.error(msg);
            throw new RuntimeException(msg);
        }
        this.eventFactory = eventFactory;
    }

    @Override
    public String toString() {
        return "StateMachineContext{" +
                "fsmEventInterceptor=" + fsmEventInterceptor +
                ", eventFactory=" + eventFactory +
                ", stateManager=" + stateManager +
                '}';
    }

}
