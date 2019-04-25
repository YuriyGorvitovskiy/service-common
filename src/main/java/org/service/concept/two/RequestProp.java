package org.service.concept.two;

public interface RequestProp {

    boolean isSelect();

    Condition getFilter();

    Sort getSort();

}
