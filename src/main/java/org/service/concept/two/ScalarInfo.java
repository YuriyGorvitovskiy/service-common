package org.service.concept.two;

import org.service.concept.Primitive;

public interface ScalarInfo {

    TypeInfo getSource();

    String getName();

    Primitive getPrimitive();

}
