package org.service.action.schema;

public interface Service {
    public static String SCHEMA     = "schema";
    public static String DEFINITION = "schema.definition";
    public static String POSTGRES   = "schema.postgres";

    public static interface Create {
        public static String COLUMN   = "create.column";
        public static String INDEX    = "create.index";
        public static String TABLE    = "create.table";
        public static String SEQUENCE = "create.sequence";
        public static String SCHEMA   = "create.schema";
    }

    public static interface Drop {
        public static String COLUMN   = "drop.column";
        public static String INDEX    = "drop.index";
        public static String TABLE    = "drop.table";
        public static String SEQUENCE = "drop.sequence";
        public static String SCHEMA   = "drop.schema";
    }
}
