package org.service.concept.immutable.command;

import org.service.concept.immutable.Event;

import com.google.common.collect.ImmutableMap;

public class TableCommand extends Event {
    public static interface Op {
        public static String INSERT = "insert";
        public static String SELECT = "select";
        public static String UPDATE = "update";
        public static String DELETE = "delete";
    }

    public final String                       table;
    public final ImmutableMap<String, Object> values;
    public final ImmutableMap<String, Object> filters;

    public TableCommand(String op,
                        String table,
                        ImmutableMap<String, Object> values,
                        ImmutableMap<String, Object> filters) {
        super(op);
        this.table = table;
        this.values = values;
        this.filters = filters;
    }
}
