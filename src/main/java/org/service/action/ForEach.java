package org.service.action;

public @interface ForEach {

    String param() default "";

    String context() default "";
}
