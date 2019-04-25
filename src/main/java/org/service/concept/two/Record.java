package org.service.concept.two;

import java.util.Map;

public interface Record {

    public TypeInfo getType();

    public Map<PropInfo, Object> getProps();

}
