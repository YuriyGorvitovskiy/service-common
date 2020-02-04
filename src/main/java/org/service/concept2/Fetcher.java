package org.service.concept2;

import java.util.Collection;

public interface Fetcher {
    Collection<Entity> fetch(Filter filter, Slice slice);
}
