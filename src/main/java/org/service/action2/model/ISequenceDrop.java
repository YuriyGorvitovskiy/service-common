package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;

@Action(ISequenceDrop.NAME)
public interface ISequenceDrop<C> extends IAction<ISequenceDrop.Params, C> {

    public final static String NAME = "schema.sequence.drop";

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
