package org.service.concept2;

public interface RelationInfo extends PropInfo {

    TypeInfo getTarget();

    RelationInfo getReverse();

    boolean isPlural();
}
