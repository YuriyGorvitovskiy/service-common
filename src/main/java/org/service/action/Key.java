package org.service.action;

public @interface Key {
    String alias() default "";

    String value();
}
