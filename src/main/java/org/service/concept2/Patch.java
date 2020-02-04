package org.service.concept2;

import java.util.Collection;
import java.util.Date;

public interface Patch extends Record {
    public enum Operation {
        UPSERT,
        UPDATE,
        DELETE
    };

    Id getUser();

    Date getTime();

    Operation getOperation();

    Collection<Filter> getFilters();
}
