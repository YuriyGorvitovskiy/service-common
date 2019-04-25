package org.service.concept.two;

import java.util.Map;

public interface TypeInfo {

    String getName();

    PropInfo getIdProp();

    Map<String, PropInfo> getProps();
}
