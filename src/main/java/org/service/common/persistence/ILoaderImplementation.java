package org.service.common.persistence;

import java.util.Collection;
import java.util.Map;

public interface ILoaderImplementation {

    public Collection<Map<String, Object>> select(String type,
                                                  String attribute,
                                                  Collection<Object> values,
                                                  Collection<String> attributes);

}
