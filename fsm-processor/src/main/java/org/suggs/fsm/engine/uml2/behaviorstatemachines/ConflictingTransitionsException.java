package org.suggs.fsm.engine.uml2.behaviorstatemachines;

/**
 * Thrown by the FSM library when more than one outgoing transition is
 * enabled from state.
 */
public class ConflictingTransitionsException extends RuntimeException {

    public ConflictingTransitionsException() {
        super();
    }

    public ConflictingTransitionsException(String message) {
        super(message);
    }

    public ConflictingTransitionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConflictingTransitionsException(Throwable cause) {
        super(cause);
    }

}
