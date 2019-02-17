package org.service.concept;

@FunctionalInterface
public interface ITranslator<S, T> {
    public T apply(S source);
}
