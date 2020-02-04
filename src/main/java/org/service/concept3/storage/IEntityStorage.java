package org.service.concept3.storage;

import io.vavr.collection.List;
import io.vavr.collection.Map;

public interface IEntityStorage {

    public default Object process(EntityRequest request) {
        if (request instanceof FetchRequest) {
            return fetch((FetchRequest) request);
        }
        if (request instanceof QueryRequest) {
            return query((QueryRequest) request);
        }
        if (request instanceof InsertRequest) {
            return insert((InsertRequest) request);
        }
        if (request instanceof UpdateRequest) {
            return update((UpdateRequest) request);
        }
        if (request instanceof UpsertRequest) {
            return upsert((UpsertRequest) request);
        }
        if (request instanceof DeleteRequest) {
            return delete((DeleteRequest) request);
        }
        return null;
    }

    public long insert(InsertRequest request);

    public long update(UpdateRequest request);

    public long upsert(UpsertRequest request);

    public long delete(DeleteRequest request);

    public List<Map<String, Object>> fetch(FetchRequest request);

    public List<Map<String, Object>> query(QueryRequest request);

}
