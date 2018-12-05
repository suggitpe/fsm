package org.suggs.fsm.engine.uml2.behaviorstatemachines;

/**
 * Thrown by the FSM library when no outgoing transitions are enabled
 * for the current event context, and the FSM does not know what to do with
 * the event.
 */
public class UnprocessableEventException extends RuntimeException {

    public UnprocessableEventException() {
        super();
    }

    public UnprocessableEventException(String message) {
        super(message);
    }

    public UnprocessableEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnprocessableEventException(Throwable cause) {
        super(cause);
    }

}
