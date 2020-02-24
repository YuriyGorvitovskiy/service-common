package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;

@Action(IIndexDrop.NAME)
public interface IIndexDrop<C> extends IAction<IIndexDrop.Params, C> {

    public final static String NAME = "schema.index.drop";

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
