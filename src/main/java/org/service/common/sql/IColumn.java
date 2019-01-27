package org.service.common.sql;

public interface IColumn {

    public String getName();

    public ITable getTable();

    public DataType getDataType();

}
