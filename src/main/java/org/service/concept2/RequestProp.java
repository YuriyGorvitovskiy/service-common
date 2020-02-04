package org.service.concept2;

public interface RequestProp {

    boolean isSelect();

    Condition getFilter();

    Sort getSort();

}
