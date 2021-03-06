package org.service.action2.patch;

import org.service.action2.Action;
import org.service.action2.IAction;
import org.service.immutable.data.Row;

@Action(IDelete.NAME)
public interface IDelete<C> extends IAction<Row, C> {

    public final static String NAME = "patch.delete";
}
