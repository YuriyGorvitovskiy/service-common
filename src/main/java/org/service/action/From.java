package org.service.action;

public @interface From {

    String schema();

    String table();

    String alias() default "";

    Equal[] on() default {};

}
