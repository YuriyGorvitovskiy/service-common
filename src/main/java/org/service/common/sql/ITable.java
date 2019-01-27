package org.service.common.sql;

import java.util.Collection;

public interface ITable {

    public String getName();

    public Collection<? extends IColumn> getColumns();

    public IColumn getColumn(String name);

}
