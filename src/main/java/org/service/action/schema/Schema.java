package org.service.action.schema;

public interface Schema {

    public static final String NAME = "model";

    public static interface Table {
        public static final String SCHEMAS       = "schemas";
        public static final String TABLES        = "tables";
        public static final String INDEXES       = "indexes";
        public static final String COLUMNS       = "columns";
        public static final String SEQUENCES     = "sequences";
        public static final String INDEX_COLUMNS = "index_columns";
    }

    public static interface Sequence {
        public static final String SCHEMA_ID   = "schema_id";
        public static final String TABLE_ID    = "table_id";
        public static final String INDEX_ID    = "indexe_id";
        public static final String COLUMN_ID   = "column_id";
        public static final String SEQUENCE_ID = "sequence_id";
    }

    public static interface Schemas {
        public static final String ID   = "id";
        public static final String NAME = "name";
    }

    public static interface Tables {
        public static final String ID     = "id";
        public static final String NAME   = "name";
        public static final String SCHEMA = "schema";
    }

    public static interface Indexes {
        public static final String ID      = "id";
        public static final String NAME    = "name";
        public static final String PRIMARY = "primary";
        public static final String TABLE   = "table";
    }

    public static interface Columns {
        public static final String ID    = "id";
        public static final String TABLE = "table";
        public static final String NAME  = "name";
        public static final String TYPE  = "type";
    }

    public static interface Sequences {
        public static final String ID     = "id";
        public static final String NAME   = "name";
        public static final String SCHEMA = "schema";
    }

    public static interface IndexColumns {
        public static final String INDEX  = "index";
        public static final String COLUMN = "column";
    }
}
