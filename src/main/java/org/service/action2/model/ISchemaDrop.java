package org.service.action2.model;

import org.service.action2.Action;
import org.service.action2.IAction;

@Action(ISchemaDrop.NAME)
public interface ISchemaDrop<C> extends IAction<ISchemaDrop.Params, C> {

    public final static String NAME = "model.schema.drop";

    public static class Params {

        public final String name;

        public Params(String name) {
            this.name = name;
        }
    }

}
