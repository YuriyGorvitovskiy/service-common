package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;
import org.service.action2.model.ISequenceCreate.Sequence;
import org.service.action2.model.ITableCreate.Table;

import io.vavr.collection.List;

@Action(ISchemaCreate.NAME)
public interface ISchemaCreate<C> extends IAction<ISchemaCreate.Params, C> {

    public final static String NAME = "model.schema.create";

    public static class Params {

        public final String         name;
        public final List<Table>    tables;
        public final List<Sequence> sequences;

        public Params(String name,
                      List<Table> tables,
                      List<Sequence> sequences) {
            this.name = name;
            this.tables = tables;
            this.sequences = sequences;
        }
    }

}
