package org.service.concept3.storage;

import io.vavr.collection.List;
import io.vavr.collection.Map;

public class FetchRequest extends EntityRequest {

    public final List<String>              select;
    public final Map<String, List<Object>> where;

    protected FetchRequest(String from, List<String> select, Map<String, List<Object>> where) {
        super(from);
        this.select = select;
        this.where = where;
    }
}
