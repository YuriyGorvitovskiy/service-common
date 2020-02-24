package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;

@Action(ITableDrop.NAME)
public interface ITableDrop<C> extends IAction<ITableDrop.Params, C> {

    public final static String NAME = "schema.table.drop";

    public static class Params {

        public final String schema;
        public final String name;

        public Params(String schema,
                      String name) {
            this.schema = schema;
            this.name = name;
        }
    }
}
