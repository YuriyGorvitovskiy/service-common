package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;

import io.vavr.collection.List;

@Action(IIndexCreate.NAME)
public interface IIndexCreate<C> extends IAction<IIndexCreate.Params, C> {

    public final static String NAME = "schema.index.create";

    public static class Params extends Index {
        public final String  schema;
        public final String  table;
        public final Boolean primary;

        public Params(String schema, String table, Boolean primary, String name, List<String> columns) {
            super(name, columns);
            this.schema = schema;
            this.table = table;
            this.primary = primary;
        }

        public Params(String schema, String table, Boolean primary, Index index) {
            this(schema, table, primary, index.name, index.columns);
        }
    }

    public static class Index {

        public final String       name;
        public final List<String> columns;

        public Index(String name, List<String> columns) {
            this.name = name;
            this.columns = columns;
        }
    }
}
