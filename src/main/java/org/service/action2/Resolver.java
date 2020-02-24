package org.service.action2;

public @interface Resolver {
    Class<IResolver<?, ?>> value();
}
