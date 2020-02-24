package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;

@Action(ISequenceCreate.NAME)
public interface ISequenceCreate<C> extends IAction<ISequenceCreate.Params, C> {

    public final static String NAME = "schema.sequence.create";

    public static class Params extends Sequence {
        public final String schema;

        public Params(String schema,
                      String name,
                      Long start) {
            super(name, start);
            this.schema = schema;
        }

        public Params(String schema, Sequence sequence) {
            this(schema, sequence.name, sequence.start);
        }
    }

    public static class Sequence {
        public final String name;
        public final Long   start;

        public Sequence(String name,
                        Long start) {
            this.name = name;
            this.start = start;
        }
    }
}
