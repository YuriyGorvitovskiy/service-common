package org.service.concept4.persistence;

import org.service.common.sql.DataType;

public class Column {
    public final String   name;
    public final DataType type;

    public Column(String name, DataType type) {
        this.name = name;
        this.type = type;
    }
}
