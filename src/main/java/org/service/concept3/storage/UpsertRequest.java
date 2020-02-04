package org.service.concept3.storage;

import io.vavr.collection.List;
import io.vavr.collection.Map;

public class UpsertRequest extends EntityRequest {

    public final Map<String, Object>       values;
    public final Map<String, List<Object>> where;

    protected UpsertRequest(String type, Map<String, Object> values, Map<String, List<Object>> where) {
        super(type);
        this.values = values;
        this.where = where;
    }
}
