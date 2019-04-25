package org.service.concept.two;

import java.util.Collection;

public interface Fetcher {
    Collection<Entity> fetch(Filter filter, Slice slice);
}
