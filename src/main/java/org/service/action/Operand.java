package org.service.action;

import org.service.immutable.schema.DataType;

public @interface Operand {
    String alias() default "";

    String column() default "";

    String param() default "";

    String context() default "";

    DataType type() default DataType.LABEL;

    String value() default "";
}
