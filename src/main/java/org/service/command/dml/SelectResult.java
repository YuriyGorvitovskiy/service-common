package org.service.command.dml;

import io.vavr.collection.Map;
import io.vavr.collection.Seq;

public class SelectResult implements DMLResult {

    public final Seq<Map<String, Object>> records;

    public SelectResult(Seq<Map<String, Object>> records) {
        this.records = records;
    }

}
