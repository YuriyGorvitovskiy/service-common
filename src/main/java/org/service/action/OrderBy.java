package org.service.action;

public @interface OrderBy {

    String column();

    boolean desc() default false;
}
