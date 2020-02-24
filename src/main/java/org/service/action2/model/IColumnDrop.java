package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;

@Action(IColumnDrop.NAME)
public interface IColumnDrop<C> extends IAction<IColumnDrop.Params, C> {

    public final static String NAME = "schema.column.drop";

    public static class Params {

        public final String schema;
        public final String table;
        public final String name;

        public Params(String schema,
                      String table,
                      String name) {
            this.schema = schema;
            this.table = table;
            this.name = name;
        }
    }
}
