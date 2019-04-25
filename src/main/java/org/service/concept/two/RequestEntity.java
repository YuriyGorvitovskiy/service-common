package org.service.concept.two;

import java.util.Map;

public interface RequestEntity {

    boolean isMust();

    Slice getSlice();

    TypeInfo getTypeInfo();

    Map<RelationInfo, RequestEntity> getRelations();

    Map<ScalarInfo, RequestProp> getProps();
}
