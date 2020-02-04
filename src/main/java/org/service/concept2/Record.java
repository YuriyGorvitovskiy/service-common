package org.service.concept2;

import java.util.Map;

public interface Record {

    public TypeInfo getType();

    public Map<PropInfo, Object> getProps();

}
