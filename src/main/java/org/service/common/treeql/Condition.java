package org.service.common.treeql;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class Condition {

    enum Operation {
        IS_NULL,
        EQUAL,
        IN,
    };

    final String       field;

    final Operation    operation;

    final List<Object> values;

    public Condition(String field, Operation operation, Object... values) {
        this.field     = field;
        this.operation = operation;
        this.values    = Arrays.asList(values);
    }

    public String toTreeQL(String indent) {
        switch (operation) {
            case EQUAL:
                return field + ": = " + values.get(0);
            case IN:
                return field + ": IN [" + String.join(", ", Lists.transform(values, (v) -> String.valueOf(v))) + "]";
            case IS_NULL:
                return field + ": IS NULL";
        }
        return null;
    }

}
