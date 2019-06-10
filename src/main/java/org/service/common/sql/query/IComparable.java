package org.service.common.sql.query;

import org.service.common.sql.DataType;
import org.service.common.sql.IPseudoSql;

public interface IComparable extends IPseudoSql {

    public DataType getDataType();

}
