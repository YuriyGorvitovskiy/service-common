package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;
import org.service.action2.model.IColumnCreate.Column;
import org.service.action2.model.IIndexCreate.Index;

import io.vavr.collection.List;

@Action(ITableCreate.NAME)
public interface ITableCreate<C> extends IAction<ITableCreate.Params, C> {

    public final static String NAME = "schema.table.create";

    public static class Params extends Table {

        public final String schema;

        public Params(String schema,
                      String name,
                      List<Column> columns,
                      Index primary,
                      List<Index> indexes) {
            super(name, columns, primary, indexes);
            this.schema = schema;
        }

        public Params(String schema, Table table) {
            this(schema, table.name, table.columns, table.primary, table.indexes);
        }
    }

    public static class Table {

        public final String       name;
        public final List<Column> columns;
        public final Index        primary;
        public final List<Index>  indexes;

        public Table(String name,
                     List<Column> columns,
                     Index primary,
                     List<Index> indexes) {
            this.name = name;
            this.columns = columns;
            this.primary = primary;
            this.indexes = indexes;
        }
    }

}
