package org.service.concept2;

import java.util.Collection;

public interface Processor {
    Collection<Patch> execute(Event event);
}
