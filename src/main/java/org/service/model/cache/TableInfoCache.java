package org.service.model.cache;

import java.util.function.Function;
import java.util.function.Predicate;

import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;

public class TableInfoCache {

    Map<TableKey, TableInfo> tables;

    public TableInfoCache(Map<TableKey, TableInfo> tables) {
        this.tables = tables;
    }

    public Set<TableKey> keys() {
        return tables.keySet();
    }

    public TableInfo get(TableKey key) {
        return tables.get(key).get();
    }

    public void put(TableKey key, TableInfo info) {
        tables = tables.put(key, info);
    }

    public TableInfo compute(TableKey key, Function<TableInfo, TableInfo> info) {
        Tuple2<Option<TableInfo>, ? extends Map<TableKey, TableInfo>> result = tables.computeIfPresent(key,
                (k, v) -> info.apply(v));
        tables = result._2;
        return result._1.get();
    }

    public void remove(TableKey key) {
        tables = tables.remove(key);
    }

    public void removeAll(Predicate<TableKey> condition) {
        tables = tables.rejectKeys(condition);
    }
}
