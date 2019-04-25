package org.service.concept.two;

import java.util.Collection;

public interface Processor {
    Collection<Patch> execute(Event event);
}
