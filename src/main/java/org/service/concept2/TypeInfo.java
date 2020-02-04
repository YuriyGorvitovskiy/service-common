package org.service.concept2;

import java.util.Map;

public interface TypeInfo {

    String getName();

    PropInfo getIdProp();

    Map<String, PropInfo> getProps();
}
