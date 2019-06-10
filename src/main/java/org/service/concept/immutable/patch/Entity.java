package org.service.concept.immutable.patch;

import com.google.common.collect.ImmutableMap;

public class Entity {

    public final String                       id;

    public final ImmutableMap<String, Object> attributes;

    public Entity(String id, ImmutableMap<String, Object> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

}
