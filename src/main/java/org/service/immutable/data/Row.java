package org.service.immutable.data;

import java.util.Objects;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public class Row {
    final String         schema;
    final String         table;
    final Map<String, ?> values;

    Row(String schema,
        String table,
        Map<String, ?> values) {

        this.schema = schema;
        this.table = table;
        this.values = values;
    }

    public boolean asBool(String column) {
        return ((Boolean) values.get(column).get()).booleanValue();
    }

    public long asLong(String column) {
        return ((Number) values.get(column).get()).longValue();
    }

    public int asInt(String column) {
        return ((Number) values.get(column).get()).intValue();
    }

    public double asDouble(String column) {
        return ((Number) values.get(column).get()).doubleValue();
    }

    public String asString(String column) {
        return Objects.toString(values.get(column).get());
    }

    @SuppressWarnings("unchecked")
    public <T> T as(String column) {
        return (T) values.get(column).get();
    }

    @SafeVarargs
    public static Row of(String schema,
                         String table,
                         Tuple2<String, ?>... values) {
        return new Row(schema, table, HashMap.ofEntries(values));
    }

    public static Row of(String schema,
                         String table,
                         Map<String, ?> values) {
        return new Row(schema, table, values);
    }
}
