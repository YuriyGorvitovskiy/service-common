package org.service.concept3.storage;

import io.vavr.collection.Map;

public class InsertRequest extends EntityRequest {

    public final Map<String, Object> values;

    protected InsertRequest(String into, Map<String, Object> values) {
        super(into);
        this.values = values;
    }

}
