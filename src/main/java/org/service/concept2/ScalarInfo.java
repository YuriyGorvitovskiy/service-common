package org.service.concept2;

import org.service.concept.Primitive;

public interface ScalarInfo {

    TypeInfo getSource();

    String getName();

    Primitive getPrimitive();

}
