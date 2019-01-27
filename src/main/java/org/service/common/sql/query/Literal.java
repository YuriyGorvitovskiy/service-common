package org.service.common.sql.query;

import java.util.Date;
import java.util.Objects;

import org.service.common.sql.DataType;
import org.service.common.util.Json;

public class Literal implements IComparable {

    public final DataType type;
    public final Object   value;

    public Literal(DataType type, Object value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public DataType getDataType() {
        return type;
    }

    @Override
    public String toString() {
        return toPseudoSql("");
    }

    @Override
    public String toPseudoSql(String indent) {
        if (null == value) {
            return "NULL";
        }
        switch (type) {
            case BOOLEAN:
                return ((Boolean) value) ? "TRUE" : "FALSE";
            case DOUBLE:
                return Double.toString(((Number) value).doubleValue());
            case INTEGER:
                return Long.toString(((Number) value).longValue());
            case REFERENCE_EXTERNAL:
                return "'" + Objects.toString(value).replaceAll("'", "''") + "'";
            case STRING:
                return "'" + Objects.toString(value).replaceAll("'", "''") + "'";
            case TEXT:
                return "'" + Objects.toString(value).replaceAll("'", "''") + "'";
            case TIMESTAMP:
                return "'" + Json.formatDate((Date) value) + "'";
        }
        return null;
    }

}
