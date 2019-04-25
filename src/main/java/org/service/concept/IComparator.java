package org.service.concept;

import java.util.function.Predicate;

public interface IComparator {
    public Predicate<Entity> getPredicate(Filter filter);
}
