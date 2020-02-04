package org.service.concept3.storage;

import io.vavr.collection.List;
import io.vavr.collection.Map;

public class DeleteRequest extends EntityRequest {

    public final Map<String, List<Object>> where;

    protected DeleteRequest(String from, Map<String, List<Object>> where) {
        super(from);
        this.where = where;
    }
}
