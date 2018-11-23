package org.suggs.fsm.engine;

import org.suggs.fsm.framework.spi.IFsmEventInterceptor;

/**
 * Defines methods for adding and removing event interceptors to and
 * from a state machine.
 */
public interface IFsmEventInterceptorManager {

    /**
     * Sets the event interceptor.
     */
    void setFsmEventInterceptor(IFsmEventInterceptor eventInterceptor);

}
