package org.suggs.fsm.engine.uml2.communications;

import org.suggs.fsm.engine.uml2.kernel.NamedElement;
import org.suggs.fsm.uml2.communications.IEvent;
import org.suggs.fsm.uml2.kernel.INamespace;

public class Event extends NamedElement implements IEvent {

    public Event(String name, INamespace namespace) {
        setName(name);
        setNamespace(namespace);
    }

}
