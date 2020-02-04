package org.service.concept2;

import java.util.Map;

public interface RequestEntity {

    boolean isMust();

    Slice getSlice();

    TypeInfo getTypeInfo();

    Map<RelationInfo, RequestEntity> getRelations();

    Map<ScalarInfo, RequestProp> getProps();
}
