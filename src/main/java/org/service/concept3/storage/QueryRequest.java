package org.service.concept3.storage;

import io.vavr.collection.List;
import io.vavr.collection.Map;

public class QueryRequest extends EntityRequest {

    public static class Range {
        public final long skip;
        public final long limit;

        public Range(long skip, long limit) {
            this.skip = skip;
            this.limit = limit;
        }
    };

    public final String                    on;
    public final List<String>              select;
    public final Map<String, QueryRequest> join;
    public final Map<String, List<Object>> where;
    public final Map<String, Boolean>      orderBy;
    public final Range                     range;

    public QueryRequest(String from,
                        String on,
                        List<String> select,
                        Map<String, QueryRequest> join,
                        Map<String, List<Object>> where,
                        Map<String, Boolean> orderBy,
                        Range range) {
        super(from);
        this.on = on;
        this.select = select;
        this.join = join;
        this.where = where;
        this.orderBy = orderBy;
        this.range = range;
    }
}
