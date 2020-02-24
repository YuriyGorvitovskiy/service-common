package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;
import org.service.immutable.schema.DataType;

@Action(IColumnCreate.NAME)
public interface IColumnCreate<C> extends IAction<IColumnCreate.Params, C> {

    public final static String NAME = "schema.column.create";

    public static class Params extends Column {

        public final String schema;
        public final String table;

        public Params(String schema,
                      String table,
                      String name,
                      DataType type) {
            super(name, type);
            this.schema = schema;
            this.table = table;
        }
    }

    public static class Column {

        public final String   name;
        public final DataType type;

        public Column(String name, DataType type) {
            this.name = name;
            this.type = type;
        }
    }
}
