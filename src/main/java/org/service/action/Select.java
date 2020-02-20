package org.service.action;

public @interface Select {

    String alias() default "";

    String value();
}
