package org.service.concept.immutable.patch;

import org.service.concept.immutable.Event;

import com.google.common.collect.ImmutableMap;

public class Patch extends Event {

    public static interface Op {
        public static String INSERT = "insert";
        public static String UPSERT = "upsert";
        public static String UPDATE = "update";
        public static String DELETE = "delete";
    }

    public final String                       id;

    public final ImmutableMap<String, Object> attributes;

    public Patch(String op, String id, ImmutableMap<String, Object> attributes) {
        super(op);
        this.id = id;
        this.attributes = attributes;
    }

}
