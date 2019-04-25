package org.service.concept.two;

public interface RelationInfo extends PropInfo {

    TypeInfo getTarget();

    RelationInfo getReverse();

    boolean isPlural();
}
