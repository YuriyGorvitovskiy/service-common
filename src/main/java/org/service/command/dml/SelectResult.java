package org.service.concept.db.event;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ResponseForSelect {

    public final ImmutableList<ImmutableMap<String, Object>> records;

    public ResponseForSelect(List<ImmutableMap<String, Object>> records) {
        this.records = ImmutableList.copyOf(records);
    }

}
