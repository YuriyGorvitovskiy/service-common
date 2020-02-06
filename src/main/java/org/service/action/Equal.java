package org.service.action;

public @interface Equal {

    String column();

    String param() default "";

    String context() default "";

    String value() default "";
}
